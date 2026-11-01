package service.impl;

import model.EventModel;
import repository.EventRepository;
import repository.impl.EventRepositoryImpl;
import service.EventService;

import java.util.List;
import java.util.Optional;

public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private EventServiceImpl(){}
    private static class Holder{
        private static final EventServiceImpl INSTANCE = new EventServiceImpl();
    }
    public static EventServiceImpl getInstance(){
        return EventServiceImpl.Holder.INSTANCE;
    }

    @Override
    public void createEvent(EventModel eventModel) {
        eventRepository.save(eventModel);
    }

    @Override
    public Optional<EventModel> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public List<EventModel> getUpcomingEvents() {
        return eventRepository.findAllUpcoming();
    }

    @Override
    public boolean updateEventById(Long id, EventModel eventModel) {
        return eventRepository.updateById(id, eventModel);
    }

    @Override
    public boolean deleteEventById(Long id) {
        return eventRepository.deleteById(id);
    }
}
