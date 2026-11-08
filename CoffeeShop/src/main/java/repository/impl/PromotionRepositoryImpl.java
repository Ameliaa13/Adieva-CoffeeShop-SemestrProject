package repository.impl;

import config.DataBaseConfig;
import mapper.PromotionMapper;
import model.PromotionModel;
import repository.PromotionRepository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PromotionRepositoryImpl implements PromotionRepository {

    private final PromotionMapper promotionMapper = new PromotionMapper();
    private PromotionRepositoryImpl() {}
    private static class Holder {
        private static final PromotionRepositoryImpl INSTANCE = new PromotionRepositoryImpl();
    }
    public static PromotionRepositoryImpl getInstance() {
        return PromotionRepositoryImpl.Holder.INSTANCE;
    }

    private static final String SQL_SAVE = """
            INSERT INTO promotions (title, short_description, description, image_url, start_date,
                                end_date, is_active) VALUES(?,?,?,?, ?,?,?);
            """;
    private static final String SQL_FIND_BY_ID = """
            SELECT * FROM promotions WHERE id = ? ;
            """;
    private static final String SQL_FIND_ALL = """
            SELECT * FROM promotions ORDER BY start_date ASC;
            """;
    private static final String SQL_DELETE_BY_ID = """
            DELETE FROM promotions WHERE id = ? ;
            """;
    private static final String SQL_UPDATE_BY_ID = """
            UPDATE promotions
            SET title = ?, short_description= ?, description = ?, image_url = ?,
                start_date = ?, end_date = ?, is_active= ? 
            WHERE id = ?; 
            """;

    private static final String SQL_FIND_ALL_ACTIVE = """
            SELECT * FROM promotions WHERE is_active = TRUE
            AND start_date <= CURRENT_DATE
                      AND end_date >= CURRENT_DATE
                    ORDER BY start_date ASC;
            """;


    @Override
    public void save(PromotionModel promotionModel) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            save(connection, promotionModel);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving promotion", e);
        }
    }

    public void save(Connection connection, PromotionModel promotionModel) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_SAVE)) {

            ps.setString(1, promotionModel.getTitle());
            ps.setString(2, promotionModel.getShortDescription());
            ps.setString(3, promotionModel.getDescription());
            ps.setString(4, promotionModel.getImageUrl());
            ps.setDate(5, Date.valueOf(promotionModel.getStartDate()));
            ps.setDate(6, Date.valueOf(promotionModel.getEndDate()));
            ps.setBoolean(7, promotionModel.isActive());

            ps.executeUpdate();

        }
    }

    @Override
    public Optional<PromotionModel> findById(Long id) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findById(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding promotion by id", e);
        }
    }

    public Optional<PromotionModel> findById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_BY_ID)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                List<PromotionModel> promotionModels = promotionMapper.toEntity(rs);

                if (promotionModels.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(promotionModels.getFirst());
            }
        }
    }

    @Override
    public List<PromotionModel> findAll() {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findAll(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all promotion", e);
        }
    }

    public List<PromotionModel> findAll(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<PromotionModel> promotionModels = promotionMapper.toEntity(rs);
                return promotionModels;
            }
        }
    }

    @Override
    public boolean deleteById(Long id) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return deleteById(connection, id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting promotion by ID", e);
        }
    }

    public boolean deleteById(Connection connection, Long id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            ps.setLong(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateById(Long id, PromotionModel promotionModel) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return updateById(connection, id, promotionModel);
        } catch (SQLException e) {
            throw new RuntimeException("Error updating promotion by ID", e);
        }
    }

    public boolean updateById(Connection connection, Long id, PromotionModel promotionModel) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_BY_ID)) {

            ps.setString(1, promotionModel.getTitle());
            ps.setString(2, promotionModel.getShortDescription());
            ps.setString(3, promotionModel.getDescription());
            ps.setString(4, promotionModel.getImageUrl());
            ps.setDate(5, Date.valueOf(promotionModel.getStartDate()));
            ps.setDate(6, Date.valueOf(promotionModel.getEndDate()));
            ps.setBoolean(7, promotionModel.isActive());
            ps.setLong(8, id);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<PromotionModel> findAllActive() {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findAllActive(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding upcoming promotion", e);
        }
    }

    public List<PromotionModel> findAllActive(Connection connection) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ALL_ACTIVE)) {
            try (ResultSet rs = ps.executeQuery()) {
                List<PromotionModel> promotionModels = promotionMapper.toEntity(rs);

                return promotionModels;
            }
        }
    }
}