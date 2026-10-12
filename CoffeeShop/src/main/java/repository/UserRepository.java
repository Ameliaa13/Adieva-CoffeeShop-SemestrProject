package repository;

import model.UserModel;

import java.util.Optional;

public interface UserRepository {

    Optional<UserModel> findByUsername(String username);
    void save(UserModel user);

}
