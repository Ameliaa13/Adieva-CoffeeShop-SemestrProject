package mapper;

import model.CartItemModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemMapper {

    private final MenuItemMapper menuItemMapper = new MenuItemMapper();

    public List<CartItemModel> toEntity(ResultSet rs) throws SQLException {
        List<CartItemModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(CartItemModel.builder()
                            .userId(rs.getLong("user_id"))
                            .menuItemId(rs.getLong("menu_item_id"))
                            .quantity(rs.getInt("quantity"))
                            .menuItemModel(menuItemMapper.toSingleEntity(rs))
                    .build());
        }
        return result;
    }
}
