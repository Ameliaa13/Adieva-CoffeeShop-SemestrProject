package service;

import model.UserModel;

import java.util.Optional;

public interface AuthService {

    void register(String username, String password);
    Optional<UserModel> authenticate(String username, String password);
    Optional<UserModel> findByUsername(String username);
}
