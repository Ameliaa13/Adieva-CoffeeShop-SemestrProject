package repository;

import model.PromotionModel;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    void save(PromotionModel promotionModel);

    Optional<PromotionModel> findById(Long id);

    List<PromotionModel> findAll();

    boolean deleteById(Long id);

    boolean updateById(Long id, PromotionModel promotionModel);

    List<PromotionModel> findAllActive();

}
