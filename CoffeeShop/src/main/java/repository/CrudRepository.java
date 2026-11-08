package repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<Model> {

    void save(Model model);

    Optional<Model> findById(Long id);

    boolean updateById(Long id, Model model);

    boolean deleteById(Long id);

    List<Model> findAll();
}
