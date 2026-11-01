package service;

import model.EventModel;

import java.util.List;
import java.util.Optional;

public interface EventService {

    void createEvent(EventModel eventModel);

    Optional<EventModel> getEventById(Long id);

    List<EventModel> getAllEvents();

    List<EventModel> getUpcomingEvents();

    boolean updateEventById(Long id, EventModel eventModel);

    boolean deleteEventById(Long id);
}
