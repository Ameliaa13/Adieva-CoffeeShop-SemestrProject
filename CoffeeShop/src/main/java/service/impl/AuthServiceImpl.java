package service.impl;

import model.UserModel;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;
import service.AuthService;
import service.PasswordEncoder;

import java.util.Optional;


public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderImpl.getInstance();
    private AuthServiceImpl(){}
    private static class Holder{
        private static final AuthServiceImpl INSTANCE = new AuthServiceImpl();
    }
    public static AuthServiceImpl getInstance(){
        return Holder.INSTANCE;
    }


    @Override
    public void register(String username, String password) {
        String hashedPassword = passwordEncoder.getHash(password);

        UserModel userModel = UserModel.builder()
                .username(username)
                .password(hashedPassword)
                .build();
        userRepository.save(userModel);
    }

    @Override
    public Optional<UserModel> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
