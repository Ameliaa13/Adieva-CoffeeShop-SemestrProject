package mapper;

import model.CategoryModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public List<CategoryModel> toEntity(ResultSet rs) throws SQLException {

        List<CategoryModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(CategoryModel.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                    .build());
        }
        return result;
    }
}
