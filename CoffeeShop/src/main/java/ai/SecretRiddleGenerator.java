package ai;

import java.util.List;

public interface SecretRiddleGenerator {

    String generate(String theme, String answer, List<String> previousRiddles);
}