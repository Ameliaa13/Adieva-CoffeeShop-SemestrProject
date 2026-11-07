package ai.impl;

import ai.SecretRiddleGenerator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FallbackSecretRiddleGenerator implements SecretRiddleGenerator {

    private static final Logger LOGGER =
            Logger.getLogger(FallbackSecretRiddleGenerator.class.getName());

    private final SecretRiddleGenerator primaryGenerator;
    private final SecretRiddleGenerator fallbackGenerator;

    public FallbackSecretRiddleGenerator(
            SecretRiddleGenerator primaryGenerator,
            SecretRiddleGenerator fallbackGenerator
    ) {
        this.primaryGenerator = primaryGenerator;
        this.fallbackGenerator = fallbackGenerator;
    }

    @Override
    public String generate(
            String theme,
            String answer,
            List<String> previousRiddles
    ) {
        try {
            return primaryGenerator.generate(theme, answer, previousRiddles);
        } catch (RuntimeException geminiError) {
            LOGGER.log(
                    Level.WARNING,
                    "Gemini не сработал. Переключаюсь на Ollama.",
                    geminiError
            );

            try {
                return fallbackGenerator.generate(
                        theme,
                        answer,
                        previousRiddles
                );
            } catch (RuntimeException ollamaError) {
                ollamaError.addSuppressed(geminiError);

                throw new IllegalStateException(
                        "Не удалось получить загадку ни от Gemini, ни от Ollama.",
                        ollamaError
                );
            }
        }
    }
}