package ai.impl;

import ai.SecretRiddleGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class OllamaSecretRiddleGenerator implements SecretRiddleGenerator {

    private static final String DEFAULT_URL = "http://localhost:11434";
    private static final String DEFAULT_MODEL = "qwen2.5:latest";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generate(String theme, String answer, List<String> previousRiddles) {
        String baseUrl = System.getenv("OLLAMA_BASE_URL");
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = DEFAULT_URL;
        }

        String model = System.getenv("OLLAMA_MODEL");
        if (model == null || model.isBlank()) {
            model = DEFAULT_MODEL;
        }

        try {
            ObjectNode requestBody = objectMapper.createObjectNode();
            requestBody.put("model", model);
            requestBody.put("prompt", createPrompt(theme, answer, previousRiddles));
            requestBody.put("stream", false);
            requestBody.put("format", "json");
            requestBody.put("keep_alive", "5m");

            String url = baseUrl.replaceAll("/+$", "") + "/api/generate";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMinutes(3))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(requestBody)
                    ))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "Ollama вернула HTTP " + response.statusCode()
                );
            }

            JsonNode responseJson = objectMapper.readTree(response.body());
            String generatedJson = responseJson.path("response").asText("").trim();

            if (generatedJson.isBlank()) {
                throw new IllegalStateException("Ollama вернула пустой ответ.");
            }

            JsonNode generated = objectMapper.readTree(generatedJson);
            String riddle = generated.path("riddle").asText("").trim();

            if (riddle.isBlank()) {
                throw new IllegalStateException(
                        "Ollama не вернула текст загадки."
                );
            }

            return riddle;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Не удалось обработать ответ Ollama.",
                    e
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Запрос к Ollama был прерван.",
                    e
            );
        }
    }

    private String createPrompt(
            String theme,
            String answer,
            List<String> previousRiddles
    ) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                Составь одну короткую интересную загадку на русском языке.
                Тема загадки: %s
                Правильный ответ: %s

                Не включай правильный ответ и название мероприятия в текст загадки.
                Не добавляй объяснение, заголовок, нумерацию или вступление.
                Загадка должна отличаться от перечисленных ниже.
                Верни только JSON такого вида: {"riddle":"текст загадки"}.
                """.formatted(theme, answer));

        if (previousRiddles != null && !previousRiddles.isEmpty()) {
            prompt.append("\nНе повторяй эти загадки:\n");

            int fromIndex = Math.max(0, previousRiddles.size() - 10);
            for (int i = fromIndex; i < previousRiddles.size(); i++) {
                prompt.append("- ")
                        .append(previousRiddles.get(i))
                        .append('\n');
            }
        }

        return prompt.toString();
    }
}