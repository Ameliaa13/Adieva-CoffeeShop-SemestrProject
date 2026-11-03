package mapper;

import model.MenuItemModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemMapper {

    public List<MenuItemModel> toEntity(ResultSet rs) throws SQLException {
        List<MenuItemModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(MenuItemModel.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .categoryId(rs.getLong("category_id"))
                            .price(rs.getDouble("price"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                    .build());
        }
        return result;
    }

    public MenuItemModel toSingleEntity(ResultSet rs) throws SQLException {
        return MenuItemModel.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .categoryId(rs.getLong("category_id"))
                .price(rs.getDouble("price"))
                .description(rs.getString("description"))
                .imageUrl(rs.getString("image_url"))
                .build();
    }
}
