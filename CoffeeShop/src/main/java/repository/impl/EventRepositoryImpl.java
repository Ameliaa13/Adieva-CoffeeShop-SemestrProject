package repository.impl;

import config.DataBaseConfig;
import mapper.EventMapper;
import model.EventModel;
import repository.EventRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class EventRepositoryImpl implements EventRepository {

    private final EventMapper eventMapper = new EventMapper();
    private EventRepositoryImpl(){}
    private static class Holder{
        private static final EventRepositoryImpl INSTANCE = new EventRepositoryImpl();
    }
    public static EventRepositoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    private static final String SQL_SAVE = """
        INSERT INTO events (
            title, description, start_time, duration_minutes, capacity,
            visibility, status, image_url, riddle_theme, answer_aliases
        )
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;
    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM events WHERE id = ? ;
            """;
    private static final String SQL_FIND_ALL = """
            SELECT * FROM events ORDER BY start_time ASC;
            """;
    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM events WHERE id = ? ;
            """;
    private static final String SQL_UPDATE_BY_ID = """
        UPDATE events
        SET title = ?, description = ?, start_time = ?, duration_minutes = ?,
            capacity = ?, visibility = ?, status = ?, image_url = ?,
            riddle_theme = ?, answer_aliases = ?
        WHERE id = ?;
        """;
    private static final String SQL_FIND_ALL_UPCOMING = """
            SELECT * FROM events WHERE status = 'PLANNED' AND visibility = 'OPEN' AND start_time >= CURRENT_TIMESTAMP
            ORDER BY start_time ASC;
            """;
    private static final String SQL_FIND_ALL_UPCOMING_FOR_MEMBERS = """
            SELECT *
            FROM events
            WHERE status = 'PLANNED'
              AND start_time >= CURRENT_TIMESTAMP
            ORDER BY start_time ASC;
            """;

    @Override
    public void save(EventModel eventModel) {
        try(Connection connection = DataBaseConfig.getConnection()){
            save(connection, eventModel);
        }catch (SQLException e){
            throw new RuntimeException("Error saving event", e);
        }
    }

    public void save(Connection connection, EventModel eventModel)throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_SAVE)){

            ps.setString(1, eventModel.getTitle());
            ps.setString(2, eventModel.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(eventModel.getStartTime()));
            ps.setObject(4, eventModel.getDurationMinutes());
            ps.setInt(5, eventModel.getCapacity());
            ps.setString(6, eventModel.getVisibility().name());
            ps.setString(7, eventModel.getStatus().name());
            ps.setString(8, eventModel.getImageUrl());
            ps.setString(9, eventModel.getRiddleTheme());
            ps.setString(10, eventModel.getAnswerAliases());

            ps.executeUpdate();

        }

    }

    @Override
    public Optional<EventModel> findById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding event by id", e);
        }
    }

    public Optional<EventModel> findById(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)){

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                List<EventModel> eventModels = eventMapper.toEntity(rs);

                if(eventModels.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(eventModels.getFirst());
            }
        }
    }

    @Override
    public List<EventModel> findAll() {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findAll(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error finding all events", e);
        }
    }

    public List<EventModel> findAll(Connection connection) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL)){
            try(ResultSet rs = ps.executeQuery()){
                List<EventModel> eventModels = eventMapper.toEntity(rs);
                return eventModels;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return deleteById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error deleting event by ID", e);
        }
    }

    public boolean deleteById(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_ID)){
            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateById(Long id, EventModel eventModel) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return updateById(connection, id, eventModel);
        }catch (SQLException e){
            throw new RuntimeException("Error updating event by ID", e);
        }
    }

    public boolean updateById(Connection connection, Long id, EventModel eventModel) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_BY_ID)){

            ps.setString(1, eventModel.getTitle());
            ps.setString(2, eventModel.getDescription());
            ps.setTimestamp(3, Timestamp.valueOf(eventModel.getStartTime()));
            ps.setObject(4, eventModel.getDurationMinutes());
            ps.setInt(5, eventModel.getCapacity());
            ps.setString(6, eventModel.getVisibility().name());
            ps.setString(7, eventModel.getStatus().name());
            ps.setString(8, eventModel.getImageUrl());
            ps.setString(9, eventModel.getRiddleTheme());
            ps.setString(10, eventModel.getAnswerAliases());
            ps.setLong(11, id);

            return ps.executeUpdate() >0;
        }
    }

    @Override
    public List<EventModel> findAllUpcoming() {
        try(Connection connection = DataBaseConfig.getConnection()) {
            return findAllUpcoming(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error finding upcoming events", e);
        }
    }

    public List<EventModel> findAllUpcoming(Connection connection) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_UPCOMING)){
            try(ResultSet rs = ps.executeQuery()){
                List<EventModel> eventModels = eventMapper.toEntity(rs);

                return eventModels;
            }
        }
    }

    @Override
    public List<EventModel> findAllUpcomingForMembers(){
        try(Connection connection = DataBaseConfig.getConnection()) {
            return findAllUpcomingForMembers(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error finding upcoming events", e);
        }
    }

    private List<EventModel> findAllUpcomingForMembers(Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_UPCOMING_FOR_MEMBERS);
             ResultSet rs = ps.executeQuery()) {
            return eventMapper.toEntity(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding upcoming events", e);
        }
    }

}
