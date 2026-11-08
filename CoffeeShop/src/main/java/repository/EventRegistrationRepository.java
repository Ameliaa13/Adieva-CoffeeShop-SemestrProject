package repository;

import enums.EventRegistrationResult;
import model.EventRegistrationModel;

public interface EventRegistrationRepository extends CrudTwoParametersRepository<EventRegistrationModel>{

    int countByEventId(Long eventId);

    boolean cancelRegistration(Long userId, Long eventId);

    EventRegistrationResult register(Long userId, Long eventId);
}
