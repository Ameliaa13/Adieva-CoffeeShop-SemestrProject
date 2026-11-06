package repository.impl;

import config.DataBaseConfig;
import enums.EventRegistrationResult;
import mapper.EventRegistrationMapper;
import model.EventRegistrationModel;
import repository.EventRegistrationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class EventRegistrationRepositoryImpl implements EventRegistrationRepository {

    private final EventRegistrationMapper eventRegistrationMapper = new EventRegistrationMapper();
    private EventRegistrationRepositoryImpl() {}
    private static class Holder {
        private static final EventRegistrationRepositoryImpl INSTANCE = new EventRegistrationRepositoryImpl();
    }
    public static EventRegistrationRepositoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static final String SQL_INSERT = """
            INSERT INTO event_registrations (event_id, user_id)
            VALUES (?, ?);
            """;

    private static final String SQL_FIND_BY_USER_ID_EVENT_ID = """
            SELECT event_id, user_id, registered_at
            FROM event_registrations
            WHERE user_id = ? AND event_id = ?;
            """;

    private static final String SQL_FIND_ALL_BY_USER_ID = """
            SELECT event_id, user_id, registered_at
            FROM event_registrations
            WHERE user_id = ?
            ORDER BY registered_at DESC;
            """;

    private static final String SQL_COUNT_BY_EVENT_ID = """
            SELECT COUNT(*)
            FROM event_registrations
            WHERE event_id = ?;
            """;

    private static final String SQL_LOCK_AVAILABLE_EVENT = """
            SELECT capacity
            FROM events
            WHERE id = ?
              AND status = 'PLANNED'
              AND visibility = 'OPEN'
              AND start_time > CURRENT_TIMESTAMP
            FOR UPDATE;
            """;

    private static final String SQL_LOCK_EVENT_FOR_CANCELLATION = """
            SELECT id
            FROM events
            WHERE id = ?
              AND start_time > CURRENT_TIMESTAMP
            FOR UPDATE;
            """;

    private static final String SQL_DELETE_BY_USER_ID_EVENT_ID = """
            DELETE FROM event_registrations
            WHERE user_id = ? AND event_id = ?;
            """;

    @Override
    public void save(Long userId, Long eventId) {
        EventRegistrationResult result = register(userId, eventId);

        if (result != EventRegistrationResult.REGISTERED) {
            throw new IllegalStateException("Не удалось записать пользователя на мероприятие: " + result);
        }
    }

    @Override
    public Optional<EventRegistrationModel> findByTwoId(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findByTwoId(connection, userId, eventId);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding event registration by user id and event id", e);
        }
    }

    public Optional<EventRegistrationModel> findByTwoId(Connection connection, Long userId, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_USER_ID_EVENT_ID)) {

            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                List<EventRegistrationModel> registrations = eventRegistrationMapper.toEntity(rs);

                if (registrations.isEmpty()) {
                    return Optional.empty();
                }

                return Optional.of(registrations.getFirst());
            }
        }
    }

    @Override
    public List<EventRegistrationModel> findAll(Long userId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findAll(connection, userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding event registrations by user id", e);
        }
    }

    public List<EventRegistrationModel> findAll(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_BY_USER_ID)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return eventRegistrationMapper.toEntity(rs);
            }
        }
    }

    @Override
    public boolean deleteByTwoId(Long userId, Long eventId) {
        return cancelRegistration(userId, eventId);
    }

    @Override
    public int countByEventId(Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return countByEventId(connection, eventId);
        } catch (SQLException e) {
            throw new RuntimeException("Error counting event registrations", e);
        }
    }

    public int countByEventId(Connection connection, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_COUNT_BY_EVENT_ID)) {

            ps.setLong(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    @Override
    public EventRegistrationResult register(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            connection.setAutoCommit(false);

            try {
                Integer capacity = findAvailableEventCapacity(connection, eventId);

                if (capacity == null) {
                    connection.rollback();
                    return EventRegistrationResult.EVENT_NOT_AVAILABLE;
                }

                if (findByTwoId(connection, userId, eventId).isPresent()) {
                    connection.rollback();
                    return EventRegistrationResult.ALREADY_REGISTERED;
                }

                int registeredCount = countByEventId(connection, eventId);

                if (registeredCount >= capacity) {
                    connection.rollback();
                    return EventRegistrationResult.FULL;
                }

                insert(connection, userId, eventId);

                connection.commit();
                return EventRegistrationResult.REGISTERED;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error registering user for event", e);
        }
    }

    private Integer findAvailableEventCapacity(Connection connection, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_LOCK_AVAILABLE_EVENT)) {

            ps.setLong(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                return rs.getInt("capacity");
            }
        }
    }

    private void insert(Connection connection, Long userId, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {

            ps.setLong(1, eventId);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }

    @Override
    public boolean cancelRegistration(Long userId, Long eventId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            connection.setAutoCommit(false);

            try {
                if (!isEventAvailableForCancellation(connection, eventId)) {
                    connection.rollback();
                    return false;
                }

                boolean deleted = deleteRegistration(connection, userId, eventId);

                connection.commit();
                return deleted;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error cancelling event registration",e);
        }
    }

    private boolean isEventAvailableForCancellation(Connection connection, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_LOCK_EVENT_FOR_CANCELLATION)) {

            ps.setLong(1, eventId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean deleteRegistration(Connection connection, Long userId, Long eventId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_USER_ID_EVENT_ID)) {

            ps.setLong(1, userId);
            ps.setLong(2, eventId);

            return ps.executeUpdate() > 0;
        }
    }
}