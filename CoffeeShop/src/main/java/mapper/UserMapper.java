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
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .firstName(rs.getString("first_name"))
                            .lastName(rs.getString("last_name"))
                            .birthDate(rs.getDate("birth_date").toLocalDate())
                            .role(rs.getString("role"))
                    .build());
        }
        return result;
    }
}
