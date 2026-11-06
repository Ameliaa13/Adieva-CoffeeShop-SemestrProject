package service;

import enums.EventRegistrationResult;
import model.EventRegistrationModel;

import java.util.Optional;

public interface EventRegistrationService {

    EventRegistrationResult registerForEvent(Long userId, Long eventId);

    Optional<EventRegistrationModel> getRegistration(Long userId, Long eventId);

    int getRegistrationCount(Long eventId);

    boolean cancelRegistration(Long userId, Long eventId);
}