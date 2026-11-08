package mapper;

import model.PromotionModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromotionMapper {

    public List<PromotionModel> toEntity(ResultSet rs) throws SQLException {
        List<PromotionModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(PromotionModel.builder()
                            .id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .shortDescription(rs.getString("short_description"))
                            .description(rs.getString("description"))
                            .imageUrl(rs.getString("image_url"))
                            .startDate(rs.getDate("start_date").toLocalDate())
                            .endDate(rs.getDate("end_date").toLocalDate())
                            .active(rs.getBoolean("is_active"))
                    .build());
        }
        return result;
    }
}
