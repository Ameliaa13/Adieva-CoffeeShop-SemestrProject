package repository.impl;

import config.DataBaseConfig;
import mapper.OrderItemMapper;
import mapper.OrderMapper;
import model.OrderItemModel;
import model.OrderModel;
import repository.OrderRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper = new OrderMapper();
    private final OrderItemMapper orderItemMapper = new OrderItemMapper();

    private OrderRepositoryImpl() {}
    private static class Holder {
        private static final OrderRepositoryImpl INSTANCE = new OrderRepositoryImpl();
    }
    public static OrderRepositoryImpl getInstance() {
        return Holder.INSTANCE;
    }

    private static final String SQL_LOCK_CART = """
            SELECT c.menu_item_id, c.quantity, m.name, m.price
            FROM cart_items c
            JOIN menu_items m ON m.id = c.menu_item_id
            WHERE c.user_id = ?
            ORDER BY m.name
            FOR UPDATE OF c;
            """;

    private static final String SQL_INSERT_ORDER = """
            INSERT INTO orders (user_id, total_price)
            VALUES (?, ?)
            RETURNING id;
            """;

    private static final String SQL_INSERT_ITEM = """
            INSERT INTO order_items (order_id, menu_item_id, item_name, unit_price, quantity)
            VALUES (?, ?, ?, ?, ?);
            """;

    private static final String SQL_DELETE_CART_ITEM = """
        DELETE FROM cart_items
        WHERE user_id = ? AND menu_item_id = ?;
        """;

    private static final String SQL_FIND_ORDER_BY_ID_AND_USER = """
            SELECT id, user_id, total_price, created_at
            FROM orders
            WHERE id = ? AND user_id = ?;
            """;

    private static final String SQL_FIND_ITEMS_BY_ORDER = """
            SELECT id, order_id, menu_item_id, item_name, unit_price, quantity
            FROM order_items
            WHERE order_id = ?
            ORDER BY id;
            """;

    private static final String SQL_FIND_ORDERS_BY_USER = """
            SELECT id, user_id, total_price, created_at
            FROM orders
            WHERE user_id = ?
            ORDER BY created_at DESC, id DESC;
            """;

    private static final String SQL_FIND_ITEMS_BY_USER = """
            SELECT oi.id, oi.order_id, oi.menu_item_id, oi.item_name, oi.unit_price, oi.quantity
            FROM order_items oi
            JOIN orders o ON o.id = oi.order_id
            WHERE o.user_id = ?
            ORDER BY oi.order_id, oi.id;
            """;


    @Override
    public Optional<Long> createFromCart(Long userId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            connection.setAutoCommit(false);
            try {
                Optional<Long> orderId = createFromCart(connection, userId);
                if (orderId.isPresent()) {
                    connection.commit();
                } else {
                    connection.rollback();
                }
                return orderId;
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating order from cart", e);
        }
    }

    public Optional<Long> createFromCart(Connection connection, Long userId) throws SQLException {
        List<OrderItemModel> items = lockCartItems(connection, userId);

        if (items.isEmpty()) {
            return Optional.empty();
        }
        BigDecimal total = items.stream()
                .map(OrderItemModel::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Long orderId = insertOrder(connection, userId, total);
        insertItems(connection, orderId, items);
        clearCart(connection, userId, items);

        return Optional.of(orderId);
    }

    private List<OrderItemModel> lockCartItems(Connection connection, Long userId) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_LOCK_CART)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                List<OrderItemModel> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(OrderItemModel.builder()
                            .menuItemId(rs.getLong("menu_item_id"))
                            .itemName(rs.getString("name"))
                            .unitPrice(rs.getBigDecimal("price"))
                            .quantity(rs.getInt("quantity"))
                            .build());
                }
                return items;
            }
        }
    }

    private Long insertOrder(Connection connection, Long userId, BigDecimal total) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ORDER)) {
            ps.setLong(1, userId);
            ps.setBigDecimal(2, total);

            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getLong("id");
            }
        }
    }

    private void insertItems(Connection connection, Long orderId, List<OrderItemModel> items) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_INSERT_ITEM)) {
            for (OrderItemModel item : items) {
                ps.setLong(1, orderId);
                ps.setLong(2, item.getMenuItemId());
                ps.setString(3, item.getItemName());
                ps.setBigDecimal(4, item.getUnitPrice());
                ps.setInt(5, item.getQuantity());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void clearCart(Connection connection, Long userId, List<OrderItemModel> items) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(SQL_DELETE_CART_ITEM)) {
            for (OrderItemModel item : items) {
                ps.setLong(1, userId);
                ps.setLong(2, item.getMenuItemId());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }


    @Override
    public Optional<OrderModel> findByIdAndUserId(Long orderId, Long userId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findByIdAndUserId(connection, orderId, userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by id and user id", e);
        }
    }

    public Optional<OrderModel> findByIdAndUserId(Connection connection, Long orderId, Long userId) throws SQLException {
        OrderModel order;
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ORDER_BY_ID_AND_USER)) {
            ps.setLong(1, orderId);
            ps.setLong(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                List<OrderModel> orders = orderMapper.toEntity(rs);
                if (orders.isEmpty()) {
                    return Optional.empty();
                }
                order = orders.getFirst();
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ITEMS_BY_ORDER)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                order.getItems().addAll(orderItemMapper.toEntity(rs));
            }
        }
        return Optional.of(order);
    }

    @Override
    public List<OrderModel> findAllByUserId(Long userId) {
        try (Connection connection = DataBaseConfig.getConnection()) {
            return findAllByUserId(connection, userId);
        } catch (SQLException e) {
            throw new RuntimeException("Error finding orders by user id", e);
        }
    }

    public List<OrderModel> findAllByUserId(Connection connection, Long userId) throws SQLException {
        Map<Long, OrderModel> ordersById = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ORDERS_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                for (OrderModel order : orderMapper.toEntity(rs)) {
                    ordersById.put(order.getId(), order);
                }
            }
        }
        if (ordersById.isEmpty()) {
            return new ArrayList<>();
        }
        try (PreparedStatement ps = connection.prepareStatement(SQL_FIND_ITEMS_BY_USER)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                for (OrderItemModel item : orderItemMapper.toEntity(rs)) {
                    OrderModel order = ordersById.get(item.getOrderId());

                    if (order != null) {
                        order.getItems().add(item);
                    }
                }
            }
        }
        return new ArrayList<>(ordersById.values());
    }
}