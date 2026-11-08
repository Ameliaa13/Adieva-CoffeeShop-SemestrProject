package service;

import model.EventModel;
import model.SecretRiddleModel;

public interface SecretRiddleService {

    EventModel getSecretEvent(Long eventId);

    SecretRiddleModel getCurrentOrGenerate(Long userId, Long eventId);

    SecretRiddleModel regenerate(Long userId, Long eventId);

    boolean checkAnswer(Long userId, Long eventId, String answer);

    boolean hasSolvedEvent(Long userId, Long eventId);
}
