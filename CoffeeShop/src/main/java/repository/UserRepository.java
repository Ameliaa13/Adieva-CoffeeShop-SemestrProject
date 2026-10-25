package repository;

import model.UserModel;

import java.util.Optional;

public interface UserRepository {

    void save(UserModel user);
    boolean existByEmail(String email);
    Optional<UserModel> findByEmail(String email);

}
