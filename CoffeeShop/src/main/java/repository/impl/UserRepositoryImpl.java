package repository.impl;

import config.DataBaseConfig;
import mapper.UserMapper;
import model.UserModel;
import repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper = new UserMapper();

    private static final String SQL_SAVE = """
            INSERT INTO users (username, password) VALUES (?, ?);
            """;
    private static final String SQL_FIND_BY_USERNAME = """
            SELECT * FROM users WHERE username = ?
            """;
    private static final String SQL_IS_EXIST = """
            SELECT COUNT(*) FROM users WHERE lower(username) = lower(?) ;
            """;

    private UserRepositoryImpl(){}
    private static class Holder{
        private static final UserRepositoryImpl INSTANCE = new UserRepositoryImpl();
    }

    public static UserRepositoryImpl getInstance(){ return Holder.INSTANCE;}

    @Override
    public Optional<UserModel> findByUsername(String username) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findByUsername(connection, username);
        }catch (SQLException e){
            throw new RuntimeException("Error finding by id"+ e);
        }

    }

    public Optional<UserModel> findByUsername(Connection connection, String username) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_USERNAME)){
            ps.setString(1, username);

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
            throw new RuntimeException("Error saving" + e);
        }
    }

    public void save(Connection connection, UserModel user) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_SAVE)){
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ps.executeUpdate();
        }
    }

    @Override
    public boolean existByUsername(String username) {
        try(Connection connection = DataBaseConfig.getConnection();
        PreparedStatement ps = connection.prepareStatement(SQL_IS_EXIST)){
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()){
                return rs.next() && rs.getInt(1) >0;
            }
        }catch (SQLException e){
            throw new RuntimeException("Error in searching ", e);
        }
    }

}
