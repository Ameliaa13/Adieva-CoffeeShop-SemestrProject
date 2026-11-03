package repository.impl;

import config.DataBaseConfig;
import mapper.FavoriteMapper;
import model.FavoriteModel;
import repository.FavoriteRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final FavoriteMapper favoriteMapper = new FavoriteMapper();

    private FavoriteRepositoryImpl(){}
    private static class Holder{
        private static FavoriteRepositoryImpl INSTANCE = new FavoriteRepositoryImpl();
    }
    public static FavoriteRepositoryImpl getInstance(){
        return FavoriteRepositoryImpl.Holder.INSTANCE;
    }

    private static final String SQL_ADD_ITEM = """
            INSERT INTO favorites (user_id, menu_item_id)
                    VALUES (?, ?)
                    ON CONFLICT (user_id, menu_item_id) DO NOTHING;
            """;
    private static final String SQL_FIND_BY_USER_ID_ITEM_ID = """
            SELECT f.user_id,
                           f.menu_item_id, m.id AS id, m.name AS name,
                           m.category_id, m.price AS price, m.description AS description,
                           m.image_url AS image_url
                    FROM favorites f
                    JOIN menu_items m ON m.id = f.menu_item_id
                    WHERE f.user_id = ? AND f.menu_item_id = ?;
            """;
    private static final String SQL_FIND_ALL_BY_USER_ID = """
            SELECT f.user_id,
                           f.menu_item_id, m.id AS id, m.name AS name,
                           m.category_id, m.price AS price, m.description AS description,
                           m.image_url AS image_url
                    FROM favorites f
                    JOIN menu_items m ON m.id = f.menu_item_id
                    WHERE f.user_id = ?
                    ORDER BY m.name;
            """;

    private static final String SQL_DELETE_ITEM = """
            DELETE FROM favorites WHERE user_id= ? AND menu_item_id=?;
            """;


    @Override
    public void addItem(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            addItem(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error adding menu item in favorites", e);
        }
    }
    public void addItem(Connection connection, Long userId, Long itemId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_ADD_ITEM)){
            ps.setLong(1, userId);
            ps.setLong(2, itemId);

            ps.executeUpdate();
        }

    }

    @Override
    public Optional<FavoriteModel> findByUserIdAndItemId(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()) {
            return findByUserIdAndItemId(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error finding item by userId and itemId in favorites", e);
        }
    }

    public Optional<FavoriteModel> findByUserIdAndItemId(Connection connection, Long userId, Long itemId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_USER_ID_ITEM_ID)){
            ps.setLong(1, userId);
            ps.setLong(2, itemId);

            try(ResultSet rs = ps.executeQuery()){
                List<FavoriteModel> result = favoriteMapper.toEntity(rs);

                if(result.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(result.getFirst());
            }
        }
    }

    @Override
    public List<FavoriteModel> findAllByUserId(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findAllByUserId(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding menu item by user id in favorites", e);
        }
    }

    public List<FavoriteModel> findAllByUserId(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_BY_USER_ID)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                List<FavoriteModel> result = favoriteMapper.toEntity(rs);
                return result;
            }
        }
    }


    @Override
    public boolean deleteItem(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return deleteItem(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error deleting item in favorites", e);
        }
    }

    public boolean deleteItem(Connection connection, Long userId, Long itemId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_DELETE_ITEM)){
            ps.setLong(1, userId);
            ps.setLong(2, itemId);

            return ps.executeUpdate()>0;
        }
    }

}
