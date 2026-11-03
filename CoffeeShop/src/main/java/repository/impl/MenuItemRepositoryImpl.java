package repository.impl;

import config.DataBaseConfig;
import mapper.MenuItemMapper;
import model.MenuItemModel;
import repository.MenuItemRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final MenuItemMapper menuItemMapper = new MenuItemMapper();

    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM menu_items WHERE id = ? ;
            """;
    private static final String SQL_FIND_BY_CATEGORY_ID = """
            SELECT * FROM menu_items WHERE category_id = ? ;
            """;
    private static final String SQL_FIND_ALL = """
            SELECT * FROM menu_items ;
            """;
    private static final String SQL_SAVE = """
            INSERT INTO menu_items (name, category_id, price, description, image_url,
            calories, proteins, fats, carbohydrates) 
            VALUES (?, ?, ?, ?,?, ?, ?, ?, ?) ;
            """;
    private static final String SQL_UPDATE_BY_ID = """
            UPDATE menu_items SET name= ?, category_id= ?, price =?,
            description = ?, image_url = ?, calories =?, proteins=?, 
            fats = ?, carbohydrates = ? WHERE id =?;
            """;
    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM menu_items WHERE id=?;
            """;

    private MenuItemRepositoryImpl(){}
    private static class Holder{
        private static final MenuItemRepositoryImpl INSTANCE = new MenuItemRepositoryImpl();
    }
    public static MenuItemRepositoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public void save(MenuItemModel menu) {
        try(Connection connection = DataBaseConfig.getConnection()) {
            save(connection,menu);
        }catch (SQLException e){
            throw new RuntimeException("Error saving menu item", e);
        }
    }

    public void save(Connection connection, MenuItemModel menu) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(SQL_SAVE)){

            ps.setString(1, menu.getName());
            ps.setLong(2, menu.getCategoryId());
            ps.setDouble(3, menu.getPrice());
            ps.setString(4, menu.getDescription());
            ps.setString(5, menu.getImageUrl());
            ps.setDouble(6, menu.getCalories());
            ps.setDouble(7, menu.getProteins());
            ps.setDouble(8, menu.getFats());
            ps.setDouble(9, menu.getCarbohydrates());

            ps.executeUpdate();
        }
    }

    @Override
    public Optional<MenuItemModel> findById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding menu item by id ", e);
        }
    }

    public Optional<MenuItemModel> findById(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()) {
                List<MenuItemModel> menuItemModels = menuItemMapper.toEntity(rs);

                if(menuItemModels.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(menuItemModels.getFirst());
            }
        }
    }

    @Override
    public List<MenuItemModel> findAll() {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findAll(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error finding menu items", e);
        }
    }

    public List<MenuItemModel> findAll(Connection connection) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL)){

            try (ResultSet rs = ps.executeQuery()){
                List<MenuItemModel> menuItemModels = menuItemMapper.toEntity(rs);
                return menuItemModels;
            }
        }
    }



    @Override
    public boolean updateById(Long id, MenuItemModel menuItem) {
        try(Connection connection= DataBaseConfig.getConnection()){
            return updateById(connection, id, menuItem);
        }catch (SQLException e){
            throw new RuntimeException("Error updating menu item by id", e);
        }
    }

    public boolean updateById(Connection connection, Long id, MenuItemModel menuItem) throws SQLException{
        try(PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_BY_ID)){
            ps.setString(1, menuItem.getName());
            ps.setLong(2, menuItem.getCategoryId());
            ps.setDouble(3, menuItem.getPrice());
            ps.setString(4, menuItem.getDescription());
            ps.setString(5, menuItem.getImageUrl());
            ps.setDouble(6, menuItem.getCalories());
            ps.setDouble(7, menuItem.getProteins());
            ps.setDouble(8, menuItem.getFats());
            ps.setDouble(9, menuItem.getCarbohydrates());
            ps.setLong(10, id);

            return ps.executeUpdate() >0;
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return deleteById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error deleting menu item by id", e);
        }
    }

    public boolean deleteById(Connection connection, Long id) throws SQLException{
        try(PreparedStatement ps =connection.prepareStatement(SQL_DELETE_BY_ID)){
            ps.setLong(1, id);

            return ps.executeUpdate()>0;
        }
    }

    @Override
    public List<MenuItemModel> findByCategoryId(Long categoryId) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findByCategoryId(connection, categoryId);
        }catch (SQLException e){
            throw new RuntimeException("Error finding menu item by category id", e);
        }
    }

    public List<MenuItemModel> findByCategoryId(Connection connection, Long categoryId) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_CATEGORY_ID)){
            ps.setLong(1, categoryId);

            try(ResultSet rs = ps.executeQuery()){
                List<MenuItemModel> menuItemModels = menuItemMapper.toEntity(rs);

                return menuItemModels;
            }
        }
    }
}
