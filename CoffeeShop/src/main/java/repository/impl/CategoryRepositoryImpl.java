package repository.impl;

import config.DataBaseConfig;
import mapper.CategoryMapper;
import model.CategoryModel;
import repository.CategoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper = new CategoryMapper();

    private static final String SQL_FIND_ALL = """
            SELECT * FROM categories;
            """;
    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM categories WHERE id = ? ;
            """;


    private CategoryRepositoryImpl(){}
    private static class Holder{
        private static final CategoryRepositoryImpl INSTANCE = new CategoryRepositoryImpl();
    }
    public static CategoryRepositoryImpl getInstance(){
        return Holder.INSTANCE;
    }

    @Override
    public List<CategoryModel> findAll() {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findAll(connection);
        }catch (SQLException e){
            throw new RuntimeException("Error finding",e);
        }
    }

    public List<CategoryModel> findAll(Connection connection) throws SQLException {
        try(PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL)){

            try (ResultSet rs = ps.executeQuery()){
                List<CategoryModel> categoryModels = categoryMapper.toEntity(rs);

                return categoryModels;
            }
        }
    }

    @Override
    public Optional<CategoryModel> findById(Long id) {
        try(Connection connection = DataBaseConfig.getConnection()){
            return findById(connection, id);
        }catch (SQLException e){
            throw new RuntimeException("Error finding by Id", e);
        }
    }

    public Optional<CategoryModel> findById(Connection connection, Long id) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)){
            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()){
                List<CategoryModel> categoryModels = categoryMapper.toEntity(rs);

                if(categoryModels.isEmpty()){
                    return Optional.empty();
                }
                return Optional.of(categoryModels.getFirst());
            }
        }
    }
}
