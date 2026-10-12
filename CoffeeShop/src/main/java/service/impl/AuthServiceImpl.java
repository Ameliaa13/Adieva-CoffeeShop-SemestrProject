package service.impl;

import lombok.RequiredArgsConstructor;
import model.UserModel;
import repository.UserRepository;
import service.AuthService;
import service.PasswordEncoder;

@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isValidUser(String username, String password) {
        return userRepository.findByUsername(username)
                .map(UserModel::getPassword)
                .map(p -> passwordEncoder.matches(password, p))
                .orElse(false);
    }
}
