package mapper;

import model.UserModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public List<UserModel> toEntity(ResultSet rs) throws SQLException {
        List<UserModel> result = new ArrayList<>();

        while (rs.next()){
            result.add(UserModel.builder()
                            .id(rs.getLong("id"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                    .build());
        }
        return result;
    }
}
