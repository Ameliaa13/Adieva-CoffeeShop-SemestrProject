package mapper;

import model.EventModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {

    public List<EventModel> toEntity(ResultSet rs) throws SQLException {
        List<EventModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(EventModel.builder()
                            .id(rs.getLong("id"))
                            .title(rs.getString("title"))
                            .description(rs.getString("description"))
                            .startTime(rs.getTimestamp("start_time").toLocalDateTime())
                            .durationMinutes(rs.getInt("duration_minutes"))
                            .capacity(rs.getInt("capacity"))
                            .visibility(rs.getString("visibility"))
                            .status(rs.getString("status"))
                            .imageUrl(rs.getString("image_url"))
                    .build());
        }
        return result;
    }
}
