package service;

import model.EventModel;
import model.UserModel;

import java.util.List;

public interface ProfileService {

    UserModel getProfile(Long userId);

    UserModel updateProfile(
            Long userId,
            String email,
            String firstName,
            String lastName,
            String birthDate
    );

    List<EventModel> getRegisteredEvents(Long userId);
}