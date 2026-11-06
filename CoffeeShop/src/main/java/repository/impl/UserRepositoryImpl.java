package repository.impl;

import config.DataBaseConfig;
import mapper.UserMapper;
import model.UserModel;
import repository.UserRepository;
import utils.ValidationUtils;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private static final String UNIQUE_VIOLATION = "23505";

    private final UserMapper userMapper = new UserMapper();

    private static final String SQL_SAVE = """
            INSERT INTO users (email, password, first_name, last_name, birth_date,role) 
            VALUES (?, ?, ?, ?, ?, ?);
            """;

    private static final String SQL_FIND_BY_EMAIL = """
            SELECT * FROM users WHERE lower(email) = lower(?);
            """;
    private static final String SQL_IS_EXIST = """
            SELECT COUNT(*) FROM users WHERE lower(email) = lower(?) ;
            """;

    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM users WHERE id = ?;
            """;
    private static final String SQL_UPDATE_BY_ID = """
            UPDATE users
            SET email = ?, first_name = ?, last_name = ?,
                birth_date = ? WHERE id = ?; 
            """;


    private UserRepositoryImpl(){}
    private static class Holder{
        private static final UserRepositoryImpl INSTANCE = new UserRepositoryImpl();
    }

    public static UserRepositoryImpl getInstance(){ return Holder.INSTANCE;}


    @Override
    public Optional<UserModel> findByEmail( String email) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findByEmail(connection, email);
        }catch (SQLException e){
            throw new RuntimeException("Error finding user by email", e);
        }
    }

    public Optional<UserModel> findByEmail(Connection connection, String email) throws SQLException{
        try(PreparedStatement ps =connection.prepareStatement(SQL_FIND_BY_EMAIL)) {
            ps.setString(1, email);

            try(ResultSet rs = ps.executeQuery()){
                List<UserModel> userModels = userMapper.toEntity(rs);

                if(userModels.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(userModels.getFirst());
            }
        }
    }

    @Override
    public void save(UserModel user) {
        try(Connection connection = DataBaseConfig.getConnection()){
            save(connection, user);
        }catch (SQLException e){
            if(UNIQUE_VIOLATION.equals(e.getSQLState())){
                throw new ValidationUtils.ValidationException( "email", "Пользователь с таким email уже существует");
            }
            throw new RuntimeException("Error saving user", e);
        }
    }

    public void save(Connection connection, UserModel user) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_SAVE)){
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFirstName());
            ps.setString(4, user.getLastName());

            if (user.getBirthDate() != null) {
                ps.setDate(5, java.sql.Date.valueOf(user.getBirthDate()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, user.getRole());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean existByEmail(String email) {
        try(Connection connection = DataBaseConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(SQL_IS_EXIST)){
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt(1) >0;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error in searching user ", e);
        }
    }


    @Override
    public Optional<UserModel> findById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding user by Id", e);
        }
    }

    public Optional<UserModel> findById(Connection connection, Long id) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                List<UserModel> userModels = userMapper.toEntity(rs);

                if(userModels.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(userModels.getFirst());
            }
        }
    }

    @Override
    public boolean updateById(Long id, UserModel userModel) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return updateById(connection, id, userModel);
        }catch (SQLException e){
            throw new RuntimeException("Error updating user by ID", e);
        }
    }

    public boolean updateById(Connection connection, Long id, UserModel userModel) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_BY_ID)){

            ps.setString(1, userModel.getEmail());
            ps.setString(2, userModel.getFirstName());
            ps.setString(3, userModel.getLastName());

            if (userModel.getBirthDate() != null) {
                ps.setDate(4, java.sql.Date.valueOf(userModel.getBirthDate()));
            } else {
                ps.setNull(4, java.sql.Types.DATE);
            }

            ps.setLong(5, id);

            return ps.executeUpdate() >0;
        }
    }
}
