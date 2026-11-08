package ai.impl;

import ai.SecretRiddleGenerator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FallbackSecretRiddleGenerator implements SecretRiddleGenerator {

    private static final Logger LOGGER = Logger.getLogger(FallbackSecretRiddleGenerator.class.getName());
    private final List<SecretRiddleGenerator> generators;
    public FallbackSecretRiddleGenerator(SecretRiddleGenerator... generators) {
        if (generators == null || generators.length == 0) {
            throw new IllegalArgumentException("Нужен хотя бы один генератор.");
        }
        this.generators = List.of(generators);
    }

    @Override
    public String generate(String theme, String answer, List<String> previousRiddles) {
        RuntimeException lastError = null;

        for (SecretRiddleGenerator generator : generators) {
            try {
                return generator.generate(theme, answer, previousRiddles);
            } catch (RuntimeException e) {
                LOGGER.log(Level.WARNING, generator.getClass().getSimpleName() + " не сработал, пробую следующий.", e);

                if (lastError != null) {
                    e.addSuppressed(lastError);
                }
                lastError = e;
            }
        }

        throw new IllegalStateException("Не удалось получить загадку ни от одного генератора.", lastError);
    }
}