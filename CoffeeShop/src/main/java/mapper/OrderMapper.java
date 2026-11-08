package mapper;

import model.OrderModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public List<OrderModel> toEntity(ResultSet rs) throws SQLException {
        List<OrderModel> result = new ArrayList<>();

        while (rs.next()) {
            result.add(OrderModel.builder()
                    .id(rs.getLong("id"))
                    .userId(rs.getLong("user_id"))
                    .totalPrice(rs.getBigDecimal("total_price"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build());
        }
        return result;
    }
}
