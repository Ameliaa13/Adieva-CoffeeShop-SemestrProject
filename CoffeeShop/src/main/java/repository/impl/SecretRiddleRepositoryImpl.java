package repository.impl;

import config.DataBaseConfig;
import mapper.SecretRiddleMapper;
import model.SecretRiddleModel;
import repository.SecretRiddleRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SecretRiddleRepositoryImpl implements SecretRiddleRepository {

    private final SecretRiddleMapper mapper = new SecretRiddleMapper();

    private static final String SQL_FIND_CURRENT = """
            SELECT *
            FROM event_riddles
            WHERE user_id = ? AND event_id = ? AND is_current = TRUE;
            """;

    private static final String SQL_FIND_HISTORY = """
            SELECT riddle_text
            FROM event_riddles
            WHERE user_id = ? AND event_id = ?
            ORDER BY created_at DESC;
            """;

    private static final String SQL_LOCK_EVENT = """
            SELECT id FROM events WHERE id = ? FOR UPDATE;
            """;

    private static final String SQL_CLEAR_CURRENT = """
            UPDATE event_riddles
            SET is_current = FALSE
            WHERE user_id = ? AND event_id = ? AND is_current = TRUE;
            """;

    private static final String SQL_INSERT = """
            INSERT INTO event_riddles
                (event_id, user_id, riddle_text, riddle_hash, is_current)
            VALUES (?, ?, ?, ?, TRUE)
            RETURNING id, event_id, user_id, riddle_text,
                      riddle_hash, created_at, is_current;
            """;

    private static final String SQL_HAS_SOLVED = """
            SELECT EXISTS (
                SELECT 1
                FROM secret_event_solutions
                WHERE user_id = ? AND event_id = ?
            );
            """;

    private static final String SQL_MARK_SOLVED = """
            INSERT INTO secret_event_solutions (event_id, user_id)
            VALUES (?, ?)
            ON CONFLICT (event_id, user_id) DO NOTHING;
            """;

    private SecretRiddleRepositoryImpl() {}

    private static class Holder {
        private static final SecretRiddleRepositoryImpl INSTANCE = new SecretRiddleRepositoryImpl();
    }

    public static SecretRiddleRepositoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public Optional<SecretRiddleModel> findCurrent(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_CURRENT)) {
            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                List<SecretRiddleModel> result = mapper.toEntity(rs);
                return result.isEmpty()
                        ? Optional.empty()
                        : Optional.of(result.getFirst());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding current secret riddle", e);
        }
    }

    @Override
    public List<String> findHistory(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_FIND_HISTORY)) {
            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                List<String> history = new ArrayList<>();

                while (rs.next()) {
                    history.add(rs.getString("riddle_text"));
                }

                return history;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding secret riddle history", e);
        }
    }

    @Override
    public boolean saveAsCurrent(Long userId, Long eventId, String riddleText, String riddleHash) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            connection.setAutoCommit(false);

            try {
                try (PreparedStatement lock = connection.prepareStatement(SQL_LOCK_EVENT)) {
                    lock.setLong(1, eventId);

                    try (ResultSet rs = lock.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement clear = connection.prepareStatement(SQL_CLEAR_CURRENT)) {
                    clear.setLong(1, userId);
                    clear.setLong(2, eventId);
                    clear.executeUpdate();
                }

                try (PreparedStatement insert = connection.prepareStatement(SQL_INSERT)) {
                    insert.setLong(1, eventId);
                    insert.setLong(2, userId);
                    insert.setString(3, riddleText);
                    insert.setString(4, riddleHash);

                    try (ResultSet rs = insert.executeQuery()) {
                        if (!rs.next()) {
                            connection.rollback();
                            return false;
                        }
                    }
                }

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();

                if ("23505".equals(e.getSQLState())) {
                    return false;
                }

                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error saving secret riddle", e);
        }
    }

    @Override
    public boolean hasSolved(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_HAS_SOLVED)) {
            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking secret event solution", e);
        }
    }

    @Override
    public void markSolved(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_MARK_SOLVED)) {
            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving secret event solution", e);
        }
    }
}
