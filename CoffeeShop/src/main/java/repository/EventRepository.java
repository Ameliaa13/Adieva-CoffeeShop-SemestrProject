package repository;

import model.EventModel;

import java.util.List;
import java.util.Optional;

public interface EventRepository {

    void save(EventModel eventModel);
    Optional<EventModel> findById(Long id);
    List<EventModel> findAll();
    boolean deleteById(Long id);
    boolean updateById(Long id, EventModel eventModel);
    List<EventModel> findAllUpcoming();

}
