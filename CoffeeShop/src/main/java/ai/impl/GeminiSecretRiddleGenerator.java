package ai.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import ai.SecretRiddleGenerator;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class GeminiSecretRiddleGenerator implements SecretRiddleGenerator {

    private static final String DEFAULT_MODEL = "gemini-3.8-flash";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generate(String theme, String answer, List<String> previousRiddles) {
        String apiKey = System.getenv("GEMINI_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("Не задана переменная окружения GEMINI_API_KEY.");
        }

        String model = System.getenv("GEMINI_MODEL");
        if (model == null || model.isBlank()) {
            model = DEFAULT_MODEL;
        }

        try {
            String prompt = createPrompt(theme, answer, previousRiddles);
            ObjectNode requestBody = createRequestBody(prompt);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL + model + ":generateContent"))
                    .timeout(Duration.ofSeconds(45))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode responseJson = objectMapper.readTree(response.body());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                String message = responseJson.path("error")
                        .path("message")
                        .asText("Gemini вернул ошибку.");

                throw new IllegalStateException("Не удалось сгенерировать загадку: " + message);
            }

            String jsonText = responseJson
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text")
                    .asText();

            if (jsonText.isBlank()) {
                throw new IllegalStateException("Gemini вернул пустой ответ.");
            }

            JsonNode generated = objectMapper.readTree(jsonText);
            String riddle = generated.path("riddle").asText("").trim();

            if (riddle.isBlank()) {
                throw new IllegalStateException("Gemini не вернул текст загадки.");
            }

            return riddle;
        } catch (IOException e) {
            throw new IllegalStateException("Ошибка при обработке ответа Gemini.", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Запрос к Gemini был прерван.", e);
        }
    }

    private String createPrompt(String theme, String answer, List<String> previousRiddles) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                Составь одну короткую, интересную загадку на русском языке.
                Тема: %s
                Загаданный ответ: %s

                Не пиши заголовок мероприятия и правильный ответ прямо в загадке.
                Не добавляй объяснение, подсказку, нумерацию или вступление.
                Верни только JSON вида {"riddle":"текст загадки"}.
                Загадка должна отличаться от всех предыдущих формулировок.
                """.formatted(theme, answer));

        if (previousRiddles != null && !previousRiddles.isEmpty()) {
            prompt.append("\nНе повторяй эти загадки и не перефразируй их:\n");

            int fromIndex = Math.max(0, previousRiddles.size() - 10);
            for (int i = fromIndex; i < previousRiddles.size(); i++) {
                prompt.append("- ")
                        .append(previousRiddles.get(i))
                        .append('\n');
            }
        }

        return prompt.toString();
    }

    private ObjectNode createRequestBody(String prompt) {
        ObjectNode root = objectMapper.createObjectNode();

        root.putArray("contents")
                .addObject()
                .putArray("parts")
                .addObject()
                .put("text", prompt);

        ObjectNode schema = objectMapper.createObjectNode();
        schema.put("type", "OBJECT");

        ObjectNode properties = schema.putObject("properties");
        properties.putObject("riddle").put("type", "STRING");

        schema.putArray("required").add("riddle");

        ObjectNode generationConfig = root.putObject("generationConfig");
        generationConfig.put("responseMimeType", "application/json");
        generationConfig.set("responseSchema", schema);

        return root;
    }
}