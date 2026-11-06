package service.impl;

import enums.EventRegistrationResult;
import model.EventRegistrationModel;
import repository.EventRegistrationRepository;
import repository.EventRepository;
import repository.impl.EventRegistrationRepositoryImpl;
import repository.impl.EventRepositoryImpl;
import service.EventRegistrationService;
import utils.ValidationUtils;

import java.util.Optional;

public class EventRegistrationServiceImpl extends ValidationUtils<EventRegistrationModel> implements EventRegistrationService {

    private final EventRegistrationRepository eventRegistrationRepository = EventRegistrationRepositoryImpl.getInstance();
    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private EventRegistrationServiceImpl() {}
    private static class Holder {
        private static final EventRegistrationServiceImpl INSTANCE = new EventRegistrationServiceImpl();
    }

    public static EventRegistrationServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validate(EventRegistrationModel model) {
        requireNonNull(model, "model");
        requireId(model.getUserId(), "userId");
        requireId(model.getEventId(), "eventId");
    }

    private EventRegistrationModel createModel(Long userId, Long eventId) {
        EventRegistrationModel model = EventRegistrationModel.builder()
                .userId(userId)
                .eventId(eventId)
                .build();

        validate(model);
        return model;
    }

    @Override
    public EventRegistrationResult registerForEvent(Long userId, Long eventId) {
        createModel(userId, eventId);

        if (eventRepository.findById(eventId).isEmpty()) {
            throw error("eventId", "Мероприятие не найдено.");
        }

        return eventRegistrationRepository.register(userId, eventId);
    }

    @Override
    public Optional<EventRegistrationModel> getRegistration(Long userId, Long eventId) {
        createModel(userId, eventId);

        return eventRegistrationRepository.findByTwoId(userId, eventId);
    }

    @Override
    public int getRegistrationCount(Long eventId) {
        requireId(eventId, "eventId");

        if (eventRepository.findById(eventId).isEmpty()) {
            throw error("eventId", "Мероприятие не найдено.");
        }

        return eventRegistrationRepository.countByEventId(eventId);
    }

    @Override
    public boolean cancelRegistration(Long userId, Long eventId) {
        createModel(userId, eventId);
        return eventRegistrationRepository.cancelRegistration(userId, eventId);
    }
}