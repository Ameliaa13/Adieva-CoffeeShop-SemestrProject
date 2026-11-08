package mapper;

import model.FavoriteModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteMapper {
    private final MenuItemMapper menuItemMapper = new MenuItemMapper();

    public List<FavoriteModel> toEntity(ResultSet rs)throws SQLException {
        List<FavoriteModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(FavoriteModel.builder()
                            .userId(rs.getLong("user_id"))
                            .menuItemId(rs.getLong("menu_item_id"))
                            .menuItemModel(menuItemMapper.toSingleEntity(rs))
                    .build());
        }
        return result;
    }
}
