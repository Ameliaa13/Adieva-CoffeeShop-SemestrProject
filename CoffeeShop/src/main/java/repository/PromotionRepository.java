package repository;

import model.PromotionModel;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends CrudRepository<PromotionModel>{

    List<PromotionModel> findAllActive();

}
