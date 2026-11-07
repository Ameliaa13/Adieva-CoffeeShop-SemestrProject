package service.impl;

import enums.EventStatus;
import enums.EventVisibility;
import model.EventModel;
import repository.EventRepository;
import repository.impl.EventRepositoryImpl;
import service.EventService;
import utils.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class EventServiceImpl extends ValidationUtils<EventModel> implements EventService {

    private static final int MAX_CAPACITY = 15;

    private final EventRepository eventRepository = EventRepositoryImpl.getInstance();
    private EventServiceImpl(){}
    private static class Holder{
        private static final EventServiceImpl INSTANCE = new EventServiceImpl();
    }
    public static EventServiceImpl getInstance(){
        return EventServiceImpl.Holder.INSTANCE;
    }



    @Override
    public void validate(EventModel eventModel) {
        requireNonNull(eventModel, "eventModel");

        String title = requireNotBlank(eventModel.getTitle(), "title").trim();
        requireMaxLength(title, 50, "title");
        eventModel.setTitle(title);
        //requireNotBlank(eventModel.getDescription(), "description");
        requireNonNull(eventModel.getStartTime(), "startTime");
        requireNonNull(eventModel.getStatus(), "status");
        requireNonNull(eventModel.getVisibility(), "visibility");
        optionalNonNegative(eventModel.getDurationMinutes(), "durationMinutes");
        requireMaxLength(eventModel.getImageUrl(), 500, "imageUrl");

        requirePositive(eventModel.getCapacity(), "capacity");
        if(eventModel.getCapacity() > MAX_CAPACITY){
            throw error("capacity",
                    "Вместимость мероприятия не может превышать " + MAX_CAPACITY + " человек.");
        }

        if (eventModel.getStatus() == EventStatus.PLANNED
                && !eventModel.getStartTime().isAfter(LocalDateTime.now())) {
            throw error(
                    "startTime",
                    "Запланированное мероприятие должно быть в будущем."
            );
        }

        if(eventModel.getVisibility() == EventVisibility.SECRET){
            String theme = requireNotBlank(eventModel.getRiddleTheme(), "riddleTheme").trim();

            requireMaxLength(theme, 250, "riddleTheme");
            eventModel.setRiddleTheme(theme);
        }

        requireMaxLength(eventModel.getAnswerAliases(), 2000, "answerAliases");
    }
    @Override
    public void createEvent(EventModel eventModel) {
        validate(eventModel);
        eventRepository.save(eventModel);
    }

    @Override
    public Optional<EventModel> getEventById(Long id) {
        requireId(id, "eventId");
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
    public List<EventModel> getUpcomingEventsForMembers() {
        return eventRepository.findAllUpcomingForMembers();
    }

    @Override
    public boolean updateEventById(Long id, EventModel eventModel) {
        requireId(id, "eventId");

        if(eventRepository.findById(id).isEmpty()){
            return false;
        }

        validate(eventModel);
        return eventRepository.updateById(id, eventModel);
    }

    @Override
    public boolean deleteEventById(Long id) {
        requireId(id, "eventId");

        if(eventRepository.findById(id).isEmpty()){
            return false;
        }
        return eventRepository.deleteById(id);
    }
}
