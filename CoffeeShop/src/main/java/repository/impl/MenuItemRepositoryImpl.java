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
            INSERT INTO menu_items (name, category_id, price, description, image_url) 
            VALUES (?, ?, ?, ?,?) ;
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
