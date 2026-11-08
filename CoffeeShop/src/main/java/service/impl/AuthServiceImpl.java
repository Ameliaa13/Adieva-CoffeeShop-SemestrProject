package service.impl;

import model.UserModel;
import repository.UserRepository;
import repository.impl.UserRepositoryImpl;
import service.AuthService;
import service.PasswordEncoder;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.util.Optional;


public class AuthServiceImpl extends ValidationUtils<UserModel> implements AuthService {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final PasswordEncoder passwordEncoder = PasswordEncoderImpl.getInstance();
    private AuthServiceImpl(){}

    private static class Holder{
        private static final AuthServiceImpl INSTANCE = new AuthServiceImpl();
    }
    public static AuthServiceImpl getInstance(){
        return Holder.INSTANCE;
    }

    private static final String DEFAULT_ROLE = "USER";

    @Override
    public void validate(UserModel userModel) {
        requireNonNull(userModel, "user");

        String email = requireNotBlank(userModel.getEmail(), "email").trim();
        requireEmail(email, "email");
        requireMaxLength(email, 50, "email");

        requirePassword(userModel.getPassword(), "password");

        requireNotBlank(userModel.getFirstName(), "firstName");
        requireMaxLength(userModel.getFirstName(), 50, "firstName");

        requireNotBlank(userModel.getLastName(), "lastName");
        requireMaxLength(userModel.getLastName(), 50, "lastName");

        requirePast(userModel.getBirthDate(), "birthDate");

    }

    @Override
    public void register(String email, String password, String firstName, String lastName, LocalDate birthDate) {

        UserModel userModel = UserModel.builder()
                .email(email == null ? null : email.trim())
                .password(password)
                .firstName(firstName == null ? null : firstName.trim())
                .lastName(lastName == null ? null : lastName.trim())
                .birthDate(birthDate)
                .role(DEFAULT_ROLE)
                .build();

        validate(userModel);

        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            throw error("email", "Пользователь с таким email уже существует");
        }

        userModel.setPassword(passwordEncoder.getHash(password));
        userRepository.save(userModel);
    }

    @Override
    public Optional<UserModel> authenticate(String email, String password) {

        requireNotBlank(email, "email");
        requireNotBlank(password, "password");

        return userRepository.findByEmail(email.trim())
                .filter(u -> passwordEncoder.matches(password, u.getPassword()));
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {

        requireNotBlank(email, "email");

        return userRepository.findByEmail(email.trim());
    }
}
