package service.impl;

import model.UserModel;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;
import service.AuthService;
import service.PasswordEncoder;

import java.time.LocalDate;
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
    public void register(String email, String password, String firstName, String lastName, LocalDate birthDate, String role) {
        String hashedPassword = passwordEncoder.getHash(password);

        UserModel userModel = UserModel.builder()
                .email(email)
                .password(hashedPassword)
                .firstName(firstName)
                .lastName(lastName)
                .birthDate(birthDate)
                .role(role)
                .build();
        userRepository.save(userModel);
    }

    @Override
    public Optional<UserModel> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
