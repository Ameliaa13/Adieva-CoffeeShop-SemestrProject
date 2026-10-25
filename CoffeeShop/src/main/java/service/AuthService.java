package service;

import model.UserModel;

import java.time.LocalDate;
import java.util.Optional;

public interface AuthService {

    void register(String email, String password, String firstName, String lastName, LocalDate birthDate, String role);
    Optional<UserModel> authenticate(String email, String password);
    Optional<UserModel> findByEmail(String email);
}
