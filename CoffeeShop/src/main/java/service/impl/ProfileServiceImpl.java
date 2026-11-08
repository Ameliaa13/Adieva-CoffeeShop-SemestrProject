package service.impl;

import model.EventModel;
import model.EventRegistrationModel;
import model.UserModel;
import repository.EventRegistrationRepository;
import repository.EventRepository;
import repository.UserRepository;
import repository.impl.EventRegistrationRepositoryImpl;
import repository.impl.EventRepositoryImpl;
import repository.impl.UserRepositoryImpl;
import service.ProfileService;
import utils.ValidationUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProfileServiceImpl extends ValidationUtils<UserModel> implements ProfileService {

    private final UserRepository userRepository = UserRepositoryImpl.getInstance();
    private final EventRegistrationRepository eventRegistrationRepository = EventRegistrationRepositoryImpl.getInstance();
    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();

    private ProfileServiceImpl() {}

    private static class Holder {
        private static final ProfileServiceImpl INSTANCE = new ProfileServiceImpl();
    }

    public static ProfileServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void validate(UserModel user) {
        requireNonNull(user, "user");

        String email = requireEmail(user.getEmail(), "email").trim();
        requireMaxLength(email, 50, "email");

        String firstName = requireNotBlank(user.getFirstName(), "firstName").trim();
        requireMaxLength(firstName, 50, "firstName");

        String lastName = requireNotBlank(user.getLastName(), "lastName").trim();
        requireMaxLength(lastName, 50, "lastName");

        requirePast(user.getBirthDate(), "birthDate");
    }

    @Override
    public UserModel getProfile(Long userId) {
        requireId(userId, "userId");

        return userRepository.findById(userId)
                .orElseThrow(() -> error("userId", "Пользователь не найден."));
    }

    @Override
    public UserModel updateProfile(Long userId, String email, String firstName, String lastName, String birthDateValue) {
        requireId(userId, "userId");

        UserModel currentUser = getProfile(userId);

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birthDateValue);
        } catch (DateTimeParseException | NullPointerException e) {
            throw error("birthDate", "Укажи корректную дату рождения.");
        }

        UserModel updatedUser = UserModel.builder()
                .id(currentUser.getId())
                .email(email == null ? null : email.trim())
                .firstName(firstName == null ? null : firstName.trim())
                .lastName(lastName == null ? null : lastName.trim())
                .birthDate(birthDate)
                .password(currentUser.getPassword())
                .role(currentUser.getRole())
                .build();

        validate(updatedUser);

        boolean emailBelongsToAnotherUser = userRepository.findByEmail(updatedUser.getEmail())
                .filter(foundUser -> !foundUser.getId().equals(userId))
                .isPresent();

        if (emailBelongsToAnotherUser) {
            throw error("email", "Пользователь с таким email уже существует.");
        }

        if (!userRepository.updateById(userId, updatedUser)) {
            throw error("userId", "Не удалось обновить профиль.");
        }

        return getProfile(userId);
    }

    @Override
    public List<EventModel> getRegisteredEvents(Long userId) {
        requireId(userId, "userId");

        List<EventModel> events = new ArrayList<>();
        List<EventRegistrationModel> registrations = eventRegistrationRepository.findAll(userId);

        for (EventRegistrationModel registration : registrations) {
            eventRepository.findById(registration.getEventId()).ifPresent(events::add);
        }

        return events;
    }
}