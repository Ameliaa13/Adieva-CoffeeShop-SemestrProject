package ai.impl;

import ai.SecretRiddleGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class GigaChatSecretRiddleGenerator implements SecretRiddleGenerator {

    private static final String DEFAULT_API_BASE_URL = "https://api.giga.chat";
    private static final String TOKEN_URL = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private static final String DEFAULT_MODEL = "GigaChat-2";
    private static final String DEFAULT_SCOPE = "GIGACHAT_API_PERS";

    private static final long TOKEN_CACHE_TIME_MILLIS = Duration.ofMinutes(29).toMillis();

    private static final int MAX_ATTEMPTS = 3;
    private static final int MIN_RIDDLE_LENGTH = 30;
    private static final int MAX_RIDDLE_LENGTH = 400;

    private static final boolean VERIFY_WITH_GUESS = false;

    private static final String SYSTEM_PROMPT = """
            Ты — опытный автор загадок для квизов и клубных мероприятий.
            Ты пишешь на грамотном, живом русском языке.
            Хорошая загадка:
            - состоит из 2–4 коротких строк или предложений;
            - описывает ответ через его характерные признаки, действия и ассоциации,
              а не называет его и не использует однокоренные слова;
            - использует метафору или неожиданный образ, но остаётся решаемой:
              человек, знающий тему, может догадаться;
            - опирается на 2–3 разных признака, а не на один очевидный;
            - не содержит общих фраз вроде «это нужно каждому» или «все его любят».
            - рассказывает о самом предмете (героях, сюжете, сценах, предметах, фактах),
              а не об обстановке, в которой о нём говорят (то есть загадка не должна строиться на "Книжный вечер", "Киновечер" и т.п.);
            """;

    private static final String EXAMPLES = """
            Примеры хорошего стиля (на другие темы, не копируй их):
            Ответ: Преступление и наказание
            {"facts":["бедный студент Раскольников","убийство старухи-процентщицы","следователь Порфирий Петрович"],"riddle":"Бедный студент придумал теорию о тех, кому «всё дозволено», и проверил её топором на старухе-процентщице. Следователь понял всё с первой беседы, а совесть — ещё раньше."}
            Ответ: Эхо
            {"facts":["нельзя увидеть и потрогать","повторяет сказанное","слышно в горах и пустых залах"],"riddle":"Меня нельзя ни увидеть, ни потрогать, но я всегда отвечаю тому, кто позвал. Ты кричишь — я вторю, только тише и позже."}

            Ответ: Шахматы
            {"facts":["поле в клетку","две армии разного цвета","король, которого нельзя съесть"],"riddle":"Две армии, чёрная и белая, воюют на поле в клетку. Короля никто не убивает, но один из них всё равно всегда проигрывает."}
            """;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile String accessToken;
    private volatile long accessTokenExpiresAt;

    @Override
    public String generate(String theme, String answer, List<String> previousRiddles) {
        String problem = null;

        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            String content = callGigaChat(
                    createPrompt(theme, answer, previousRiddles, problem),
                    0.8
            );
            String riddle = extractRiddle(content);

            problem = validate(riddle, answer, previousRiddles);

            if (problem == null && VERIFY_WITH_GUESS && !canBeGuessed(theme, answer, riddle)) {
                problem = "По этой загадке нельзя догадаться об ответе: она слишком "
                        + "расплывчатая или уходит от темы. Опиши ответ точнее через "
                        + "его характерные признаки.";
            }

            if (problem == null) {
                return riddle;
            }
        }

        throw new IllegalStateException(
                "Не удалось сгенерировать подходящую загадку. Последняя проблема: " + problem
        );
    }

    private String callGigaChat(String prompt, double temperature) {
        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", getEnvironmentValue("GIGACHAT_MODEL", DEFAULT_MODEL));
            requestBody.put("temperature", temperature);
            requestBody.put("top_p", 0.9);
            requestBody.put("max_tokens", 500);

            ArrayNode messages = requestBody.putArray("messages");
            messages.addObject()
                    .put("role", "system")
                    .put("content", SYSTEM_PROMPT);
            messages.addObject()
                    .put("role", "user")
                    .put("content", prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(getApiBaseUrl() + "/v1/chat/completions"))
                    .timeout(Duration.ofSeconds(60))
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("User-Agent", "CoffeeShop/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException("GigaChat вернул HTTP " + response.statusCode() + ": " + shorten(response.body()));
            }

            JsonNode responseJson = objectMapper.readTree(response.body());
            String content = responseJson
                    .path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("")
                    .trim();

            if (content.isBlank()) {
                throw new IllegalStateException("GigaChat вернул пустой ответ.");
            }
            return content;

        } catch (IOException e) {
            throw new IllegalStateException("Не удалось обработать ответ GigaChat.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Запрос к GigaChat был прерван.", e);
        }
    }

    private String getAccessToken() {
        if (accessToken != null && System.currentTimeMillis() < accessTokenExpiresAt) {
            return accessToken;
        }

        synchronized (this) {
            if (accessToken != null && System.currentTimeMillis() < accessTokenExpiresAt) {
                return accessToken;
            }

            String authorizationKey = System.getenv("GIGACHAT_AUTH_KEY");

            if (authorizationKey == null || authorizationKey.isBlank()) {
                throw new IllegalStateException("Не задана переменная окружения GIGACHAT_AUTH_KEY.");
            }

            String scope = getEnvironmentValue("GIGACHAT_SCOPE", DEFAULT_SCOPE);

            String authorizationHeader = authorizationKey.trim();
            if (!authorizationHeader.startsWith("Basic ")) {
                authorizationHeader = "Basic " + authorizationHeader;
            }

            String form = "scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(TOKEN_URL))
                    .timeout(Duration.ofSeconds(30))
                    .header("Authorization", authorizationHeader)
                    .header("RqUID", UUID.randomUUID().toString())
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Accept", "application/json")
                    .header("User-Agent", "CoffeeShop/1.0")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            try {
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

                if (response.statusCode() < 200 || response.statusCode() >= 300) {
                    throw new IllegalStateException("Не удалось получить токен GigaChat. HTTP "
                            + response.statusCode()
                            + ": " + shorten(response.body()));
                }
                JsonNode tokenResponse = objectMapper.readTree(response.body());

                String newAccessToken = tokenResponse
                        .path("access_token")
                        .asText("")
                        .trim();

                if (newAccessToken.isBlank()) {
                    throw new IllegalStateException("GigaChat не вернул access token.");
                }

                accessToken = newAccessToken;
                accessTokenExpiresAt = System.currentTimeMillis() + TOKEN_CACHE_TIME_MILLIS;

                return accessToken;
            } catch (IOException e) {
                throw new IllegalStateException("Не удалось обработать ответ авторизации GigaChat.", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Запрос токена GigaChat был прерван.", e);
            }
        }
    }


    private String createPrompt(String theme, String answer, List<String> previousRiddles, String previousProblem) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
             Составь одну загадку на русском языке.
        
             Предмет загадки: %s
             Правильный ответ: %s
        
             Порядок работы:
             1. В поле "facts" перечисли 3–4 конкретных факта о предмете: главные герои
                или участники, ключевые события и сцены, известные предметы, места, цитаты.
                Только настоящие факты, ничего не выдумывай.
             2. В поле "riddle" напиши загадку, опираясь на 2–3 из этих фактов.
        
             Требования к загадке:
             - она описывает сам предмет, а не обстановку вокруг него: не пиши про вечер,
               встречу, чтение, любителей, зрителей и атмосферу;
             - не называй правильный ответ и однокоренные слова;
             - в ней должны быть конкретные детали, по которым предмет можно узнать;
             - без пояснений, заголовка, нумерации и вступления.
        
             Верни только JSON такого вида:
             {"facts":["факт 1","факт 2","факт 3"],"riddle":"текст загадки"}
        
        """.formatted(theme, answer));

        prompt.append(EXAMPLES);

        if (previousRiddles != null && !previousRiddles.isEmpty()) {
            prompt.append("\nНе повторяй эти загадки и их образы:\n");

            int fromIndex = Math.max(0, previousRiddles.size() - 10);
            for (int i = fromIndex; i < previousRiddles.size(); i++) {
                prompt.append("- ")
                        .append(previousRiddles.get(i))
                        .append('\n');
            }
        }

        if (previousProblem != null) {
            prompt.append("\nПредыдущая попытка отклонена. Причина: ")
                    .append(previousProblem)
                    .append("\nИсправь это в новой загадке.\n");
        }

        return prompt.toString();
    }


    private JsonNode parseJson(String content) {
        String cleaned = stripCodeFence(content);
        try {
            return objectMapper.readTree(cleaned);
        } catch (IOException e) {
            return null;
        }
    }

    private String stripCodeFence(String content) {
        String cleaned = content.trim();

        if (cleaned.startsWith("```")) {
            int firstLineEnd = cleaned.indexOf('\n');

            if (firstLineEnd >= 0) {
                cleaned = cleaned.substring(firstLineEnd + 1);
            }

            if (cleaned.endsWith("```")) {
                cleaned = cleaned.substring(0, cleaned.length() - 3);
            }

            cleaned = cleaned.trim();
        }
        return cleaned;
    }

    private String extractRiddle(String content) {
        JsonNode generated = parseJson(content);

        if (generated == null || !generated.isObject()) {
            return stripCodeFence(content);
        }
        return generated.path("riddle").asText("").trim();
    }

    private String validate(String riddle, String answer, List<String> previousRiddles) {
        if (riddle.isBlank()) {
            return "Текст загадки пустой или в ответе нет поля riddle.";
        }
        if (riddle.length() < MIN_RIDDLE_LENGTH) {
            return "Загадка слишком короткая.";
        }
        if (riddle.length() > MAX_RIDDLE_LENGTH) {
            return "Загадка слишком длинная, сократи её до 2–4 предложений.";
        }

        String normalizedRiddle = normalize(riddle);
        for (String token : tokens(answer)) {
            if (containsToken(normalizedRiddle, token)) {
                return "В тексте загадки есть правильный ответ или слово «" + token
                        + "». Опиши ответ, не называя его.";
            }
        }

        if (previousRiddles != null) {
            Set<String> words = new HashSet<>(Arrays.asList(normalizedRiddle.split(" ")));
            for (String previous : previousRiddles) {
                Set<String> previousWords =
                        new HashSet<>(Arrays.asList(normalize(previous).split(" ")));
                if (similarity(words, previousWords) > 0.6) {
                    return "Загадка слишком похожа на одну из предыдущих. "
                            + "Используй другие образы.";
                }
            }
        }
        return null;
    }

    private boolean canBeGuessed(String theme, String answer, String riddle) {
        String prompt = """
                Вот загадка. Тема: %s

                Загадка: %s

                Отгадай её. Ответ должен быть коротким: слово или название.
                Верни только JSON такого вида: {"guess":"твой ответ"}
                """.formatted(theme, riddle);

        try {
            String content = callGigaChat(prompt, 0.2);
            JsonNode result = parseJson(content);
            String guessText = (result != null && result.isObject())
                    ? result.path("guess").asText("")
                    : content;
            String guess = normalize(guessText);

            if (guess.isBlank()) {
                return false;
            }
            for (String token : tokens(answer)) {
                if (!containsToken(guess, token)) {
                    return false;
                }
            }
            return true;
        } catch (IllegalStateException e) {
            return true;
        }
    }


    private static String normalize(String text) {
        return text == null ? "" : text
                .toLowerCase(Locale.ROOT)
                .replace('ё', 'е')
                .replaceAll("[^\\p{L}\\p{N}\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private static List<String> tokens(String answer) {
        return Arrays.stream(normalize(answer).split(" "))
                .filter(token -> token.length() >= 3)
                .toList();
    }

    private static boolean containsToken(String normalizedText, String token) {
        if (token.length() < 5) {
            return (" " + normalizedText + " ").contains(" " + token + " ");
        }
        return normalizedText.contains(token.substring(0, token.length() - 2));
    }

    private static double similarity(Set<String> a, Set<String> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return 0;
        }
        Set<String> intersection = new HashSet<>(a);
        intersection.retainAll(b);
        Set<String> union = new HashSet<>(a);
        union.addAll(b);
        return (double) intersection.size() / union.size();
    }

    private String getApiBaseUrl() {
        return getEnvironmentValue("GIGACHAT_API_BASE_URL", DEFAULT_API_BASE_URL).replaceAll("/+$", "");
    }

    private String getEnvironmentValue(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank()
                ? defaultValue
                : value.trim();
    }

    private String shorten(String value) {
        if (value == null || value.length() <= 500) {
            return value;
        }
        return value.substring(0, 500);
    }
}