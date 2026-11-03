package repository;

import model.EventModel;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CrudRepository<EventModel>{

    List<EventModel> findAllUpcoming();

}
