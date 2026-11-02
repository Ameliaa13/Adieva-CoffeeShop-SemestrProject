package service.impl;

import model.PromotionModel;
import repository.PromotionRepository;
import repository.impl.PromotionRepositoryImpl;
import service.PromotionService;

import java.util.List;
import java.util.Optional;

public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private PromotionServiceImpl() {}

    private static class Holder {
        private static final PromotionServiceImpl INSTANCE = new PromotionServiceImpl();
    }

    public static PromotionServiceImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void createPromotion(PromotionModel promotionModel) {
        promotionRepository.save(promotionModel);
    }

    @Override
    public Optional<PromotionModel> getPromotionById(Long id) {
        return promotionRepository.findById(id);
    }

    @Override
    public List<PromotionModel> getAllPromotions() {
        return promotionRepository.findAll();
    }

    @Override
    public List<PromotionModel> getActivePromotions() {
        return promotionRepository.findAllActive();
    }

    @Override
    public boolean updatePromotionById(Long id, PromotionModel promotionModel) {
        return promotionRepository.updateById(id, promotionModel);
    }

    @Override
    public boolean deletePromotionById(Long id) {
        return promotionRepository.deleteById(id);
    }
}
