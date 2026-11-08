package service;

import model.PromotionModel;

import java.util.List;
import java.util.Optional;

public interface PromotionService {

    void createPromotion(PromotionModel promotionModel);

    Optional<PromotionModel> getPromotionById(Long id);

    List<PromotionModel> getAllPromotions();

    List<PromotionModel> getActivePromotions();

    boolean updatePromotionById(Long id, PromotionModel promotionModel);

    boolean deletePromotionById(Long id);
}
