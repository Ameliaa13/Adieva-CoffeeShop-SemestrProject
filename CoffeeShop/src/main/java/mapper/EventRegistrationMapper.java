package mapper;

import model.EventRegistrationModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventRegistrationMapper {

    public List<EventRegistrationModel> toEntity(ResultSet rs) throws SQLException {
        List<EventRegistrationModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(EventRegistrationModel.builder()
                            .eventId(rs.getLong("event_id"))
                            .userId(rs.getLong("user_id"))
                            .registeredAt(rs.getTimestamp("registered_at").toLocalDateTime())
                    .build());
        }
        return result;
    }
}
