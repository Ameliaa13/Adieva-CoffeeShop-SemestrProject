package mapper;

import model.SecretRiddleModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SecretRiddleMapper {

    public List<SecretRiddleModel> toEntity(ResultSet rs) throws SQLException {
        List<SecretRiddleModel> riddles = new ArrayList<>();

        while (rs.next()) {
            riddles.add(SecretRiddleModel.builder()
                    .id(rs.getLong("id"))
                    .eventId(rs.getLong("event_id"))
                    .userId(rs.getLong("user_id"))
                    .riddleText(rs.getString("riddle_text"))
                    .riddleHash(rs.getString("riddle_hash"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .current(rs.getBoolean("is_current"))
                    .build());
        }

        return riddles;
    }
}