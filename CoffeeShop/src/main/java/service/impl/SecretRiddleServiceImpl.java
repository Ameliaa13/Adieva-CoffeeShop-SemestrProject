package service.impl;

import enums.EventStatus;
import enums.EventVisibility;
import model.EventModel;
import model.SecretRiddleModel;
import repository.EventRepository;
import repository.SecretRiddleRepository;
import repository.impl.EventRepositoryImpl;
import repository.impl.SecretRiddleRepositoryImpl;
import ai.SecretRiddleGenerator;
import service.SecretRiddleService;
import utils.ValidationUtils;
import ai.impl.FallbackSecretRiddleGenerator;
import ai.impl.GeminiSecretRiddleGenerator;
import ai.impl.OllamaSecretRiddleGenerator;


import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class SecretRiddleServiceImpl
        extends ValidationUtils<SecretRiddleModel>
        implements SecretRiddleService {

    private static final int MAX_GENERATION_ATTEMPTS = 5;

    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private final SecretRiddleRepository riddleRepository = SecretRiddleRepositoryImpl.getInstance();

    private final SecretRiddleGenerator riddleGenerator = new FallbackSecretRiddleGenerator(new GeminiSecretRiddleGenerator(), new OllamaSecretRiddleGenerator()
    );

    private SecretRiddleServiceImpl() {}

    private static class Holder {
        private static final SecretRiddleServiceImpl INSTANCE = new SecretRiddleServiceImpl();
    }

    public static SecretRiddleServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validate(SecretRiddleModel model) {
        requireNonNull(model, "riddle");
        requireId(model.getUserId(), "userId");
        requireId(model.getEventId(), "eventId");
        requireNotBlank(model.getRiddleText(), "riddleText");
        requireMaxLength(model.getRiddleText(), 1000, "riddleText");
    }

    @Override
    public EventModel getSecretEvent(Long eventId) {
        requireId(eventId, "eventId");

        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(() -> error("eventId", "Мероприятие не найдено."));

        if (event.getVisibility() != EventVisibility.SECRET) {
            throw error("eventId", "Это не секретное мероприятие.");
        }

        if (event.getStatus() != EventStatus.PLANNED
                || !event.getStartTime().isAfter(LocalDateTime.now())) {
            throw error("eventId", "Это мероприятие сейчас недоступно.");
        }

        requireNotBlank(event.getRiddleTheme(), "riddleTheme");
        return event;
    }

    @Override
    public SecretRiddleModel getCurrentOrGenerate(Long userId, Long eventId) {
        requireId(userId, "userId");
        EventModel event = getSecretEvent(eventId);

        Optional<SecretRiddleModel> current = riddleRepository.findCurrent(userId, eventId);

        return current.orElseGet(() -> generateUnique(userId, event));
    }

    @Override
    public SecretRiddleModel regenerate(Long userId, Long eventId) {
        requireId(userId, "userId");
        return generateUnique(userId, getSecretEvent(eventId));
    }

    private SecretRiddleModel generateUnique(Long userId, EventModel event) {
        List<String> history = riddleRepository.findHistory(userId, event.getId());

        for (int attempt = 0; attempt < MAX_GENERATION_ATTEMPTS; attempt++) {
            String text = riddleGenerator.generate(
                    event.getRiddleTheme(),
                    event.getTitle(),
                    history
            ).trim();

            SecretRiddleModel draft = SecretRiddleModel.builder()
                    .eventId(event.getId())
                    .userId(userId)
                    .riddleText(text)
                    .build();

            validate(draft);
            if (containsAnswer(text, event)) {
                history.add(text);
                continue;
            }

            String riddleHash = createHash(text);

            boolean saved = riddleRepository.saveAsCurrent(
                    userId,
                    event.getId(),
                    text,
                    riddleHash
            );

            if (saved) {
                return riddleRepository.findCurrent(userId, event.getId())
                        .orElseThrow(() -> new IllegalStateException("Загадка не найдена после сохранения."));
            }

            history.add(text);
        }

        throw new IllegalStateException("Не удалось получить новую уникальную загадку. Попробуй ещё раз.");
    }

    @Override
    public boolean checkAnswer(Long userId, Long eventId, String answer) {
        requireId(userId, "userId");
        EventModel event = getSecretEvent(eventId);
        getCurrentOrGenerate(userId, eventId);

        String normalizedAnswer = normalize(requireNotBlank(answer, "answer"));

        if (normalizedAnswer.length() > 200) {
            throw error("answer", "Ответ должен быть не длиннее 200 символов.");
        }

        List<String> acceptedAnswers = new ArrayList<>();
        acceptedAnswers.add(event.getTitle());

        if (event.getAnswerAliases() != null) {
            for (String alias : event.getAnswerAliases().split("\\R")) {
                if (!alias.isBlank()) {
                    acceptedAnswers.add(alias);
                }
            }
        }

        for (String accepted : acceptedAnswers) {
            String normalizedAccepted = normalize(accepted);

            if (!normalizedAccepted.isBlank()
                    && (" " + normalizedAnswer + " ").contains(" " + normalizedAccepted + " ")) {
                riddleRepository.markSolved(userId, eventId);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasSolvedEvent(Long userId, Long eventId) {
        requireId(userId, "userId");
        requireId(eventId, "eventId");
        return riddleRepository.hasSolved(userId, eventId);
    }

    private boolean containsAnswer(String riddleText, EventModel event) {
        String normalizedRiddle = " " + normalize(riddleText) + " ";

        List<String> answers = new ArrayList<>();
        answers.add(event.getTitle());

        if (event.getAnswerAliases() != null) {
            for (String alias : event.getAnswerAliases().split("\\R")) {
                if (!alias.isBlank()) {
                    answers.add(alias);
                }
            }
        }

        for (String answer : answers) {
            String normalizedAnswer = normalize(answer);

            if (!normalizedAnswer.isBlank() && normalizedRiddle.contains(" " + normalizedAnswer + " ")) {
                return true;
            }
        }

        return false;
    }

    private String normalize(String value) {
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFKD)
                .toLowerCase(Locale.ROOT)
                .replace('ё', 'е');

        return normalized
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^\\p{L}\\p{N}]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private String createHash(String text) {
        return "a" + normalize(text).hashCode();
    }

}
