package repository;

import java.util.List;
import java.util.Optional;

public interface CrudTwoParametersRepository<Model> {

    void save(Long userId, Long modelId);

    Optional<Model> findByTwoId(Long userId, Long modelId);

    List<Model> findAll(Long userId);

    boolean deleteByTwoId(Long userId, Long modelId);
}
