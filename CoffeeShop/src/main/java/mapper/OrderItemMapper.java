package mapper;

import model.OrderItemModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemMapper {

    public List<OrderItemModel> toEntity(ResultSet rs) throws SQLException {
        List<OrderItemModel> result = new ArrayList<>();

        while (rs.next()) {
            long menuItemId = rs.getLong("menu_item_id");
            Long menuItem = rs.wasNull() ? null : menuItemId;

            result.add(OrderItemModel.builder()
                    .id(rs.getLong("id"))
                    .orderId(rs.getLong("order_id"))
                    .menuItemId(menuItem)
                    .itemName(rs.getString("item_name"))
                    .unitPrice(rs.getBigDecimal("unit_price"))
                    .quantity(rs.getInt("quantity"))
                    .build());
        }
        return result;
    }
}
