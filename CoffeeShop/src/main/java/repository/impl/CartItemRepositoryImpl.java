package repository.impl;

import config.DataBaseConfig;
import mapper.CartItemMapper;
import model.CartItemModel;
import repository.CartItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CartItemRepositoryImpl implements CartItemRepository {

    private final CartItemMapper cartItemMapper = new CartItemMapper();

    private CartItemRepositoryImpl(){}
    private static class Holder{
        private static final CartItemRepositoryImpl INSTANCE = new CartItemRepositoryImpl();
    }
    public static CartItemRepositoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    private static final String SQL_ADD_ITEM = """
            INSERT INTO cart_items (user_id, menu_item_id, quantity)
                    VALUES (?, ?, 1)
                    ON CONFLICT (user_id, menu_item_id)
                    DO UPDATE SET quantity = cart_items.quantity + 1;
            """;
    private static final String SQL_FIND_BY_USER_ID_ITEM_ID = """
            SELECT c.user_id,
                     c.menu_item_id,
                     c.quantity,
                     m.id AS id,
                     m.name AS name,
                     m.category_id,
                     m.price AS price,
                     m.description AS description,
                     m.image_url AS image_url,
                     m.calories,
                     m.proteins,
                     m.fats,
                     m.carbohydrates
                 FROM cart_items c
                 JOIN menu_items m ON m.id = c.menu_item_id
                 WHERE c.user_id = ? AND c.menu_item_id = ?;
            """;
    private static final String SQL_FIND_ALL_BY_USER_ID = """
            SELECT c.user_id,
                      c.menu_item_id,
                      c.quantity,
                      m.id AS id,
                      m.name AS name,
                      m.category_id,
                      m.price AS price,
                      m.description AS description,
                      m.image_url AS image_url,
                      m.calories,
                      m.proteins,
                      m.fats,
                      m.carbohydrates
                 FROM cart_items c
                 JOIN menu_items m ON m.id = c.menu_item_id
                 WHERE c.user_id = ?
                 ORDER BY m.name;
            """;
    private static final String SQL_UPDATE_QUANTITY = """
            UPDATE cart_items SET quantity = ?
            WHERE user_id= ? AND menu_item_id=?;
            """;
    private static final String SQL_DELETE_ITEM = """
            DELETE FROM cart_items WHERE user_id= ? AND menu_item_id=?;
            """;
    private static final String SQL_CLEAR_BY_USER_ID = """
            DELETE FROM cart_items WHERE user_id= ?;
            """;


    @Override
    public void save(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            addItem(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error adding menu item in cart ", e);
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
    public Optional<CartItemModel> findByTwoId(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()) {
            return findByTwoId(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error finding item by userId and itemId in cart", e);
        }
    }

    public Optional<CartItemModel> findByTwoId(Connection connection, Long userId, Long itemId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_USER_ID_ITEM_ID)){
            ps.setLong(1, userId);
            ps.setLong(2, itemId);

            try(ResultSet rs = ps.executeQuery()){
                List<CartItemModel> result = cartItemMapper.toEntity(rs);

                if(result.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(result.getFirst());
            }
        }
    }

    @Override
    public List<CartItemModel> findAll(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findAll(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding menu item by user id in cart", e);
        }
    }

    public List<CartItemModel> findAll(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_BY_USER_ID)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                List<CartItemModel> result = cartItemMapper.toEntity(rs);
                return result;
            }
        }
    }

    @Override
    public boolean updateQuantity(Long userId, Long itemId, Integer quantity) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return updateQuantity(connection,userId, itemId, quantity);
        }catch (SQLException e){
            throw new RuntimeException("Error updating item quantity in cart", e);
        }
    }

    public boolean updateQuantity(Connection connection, Long userId, Long itemId, Integer quantity) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_QUANTITY)){
            ps.setInt(1, quantity);
            ps.setLong(2, userId);
            ps.setLong(3, itemId);


            return ps.executeUpdate() >0;
        }
    }

    @Override
    public boolean deleteByTwoId(Long userId, Long itemId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return deleteByTwoId(connection, userId, itemId);
        }catch (SQLException e){
            throw new RuntimeException("Error deleting item in cart", e);
        }
    }

    public boolean deleteByTwoId(Connection connection, Long userId, Long itemId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_DELETE_ITEM)){
            ps.setLong(1, userId);
            ps.setLong(2, itemId);

            return ps.executeUpdate()>0;
        }
    }

    @Override
    public boolean clearByUserId(Long userId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return clearByUserId(connection, userId);
        }catch (SQLException e){
            throw new RuntimeException("Error clearing cart in cart",e);
        }
    }

    public boolean clearByUserId(Connection connection, Long userId) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_CLEAR_BY_USER_ID)){
            ps.setLong(1, userId);

            return ps.executeUpdate()>0;
        }
    }
}
