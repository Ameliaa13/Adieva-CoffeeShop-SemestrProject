package repository;

import model.SecretRiddleModel;

import java.util.List;
import java.util.Optional;

public interface SecretRiddleRepository {

    Optional<SecretRiddleModel> findCurrent(Long userId, Long eventId);

    List<String> findHistory(Long userId, Long eventId);

    boolean saveAsCurrent(Long userId, Long eventId, String riddleText, String riddleHash);

    boolean hasSolved(Long userId, Long eventId);

    void markSolved(Long userId, Long eventId);
}
