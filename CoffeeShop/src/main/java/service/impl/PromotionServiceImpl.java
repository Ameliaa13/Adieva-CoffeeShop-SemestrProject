package service.impl;

import model.PromotionModel;
import repository.PromotionRepository;
import repository.impl.PromotionRepositoryImpl;
import service.PromotionService;
import utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class PromotionServiceImpl extends ValidationUtils<PromotionModel> implements PromotionService {

    private final PromotionRepository promotionRepository = PromotionRepositoryImpl.getInstance();
    private PromotionServiceImpl() {}

    private static class Holder {
        private static final PromotionServiceImpl INSTANCE = new PromotionServiceImpl();
    }

    public static PromotionServiceImpl getInstance() {
        return Holder.INSTANCE;
    }



    @Override
    public void validate(PromotionModel promotionModel) {
        requireNonNull(promotionModel, "promotionModel");
        requireNotBlank(promotionModel.getTitle(), "title");
        requireMaxLength(promotionModel.getTitle(), 100, "title");
        requireMaxLength(promotionModel.getShortDescription(), 250, "shortDescription");
        requireNotBlank(promotionModel.getDescription(), "description");

        requireMaxLength(promotionModel.getImageUrl(),256, "imageUrl");
        requireNonNull(promotionModel.getStartDate(), "startDate");
        requireDateNotBefore(promotionModel.getStartDate(), promotionModel.getEndDate(), "endDate");

    }

    @Override
    public void createPromotion(PromotionModel promotionModel) {
        validate(promotionModel);
        promotionRepository.save(promotionModel);
    }

    @Override
    public Optional<PromotionModel> getPromotionById(Long id) {
        requireId(id, "promotionId");
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
        requireId(id, "promotionId");
        if(promotionRepository.findById(id).isEmpty()){
            return false;
        }
        validate(promotionModel);

        return promotionRepository.updateById(id, promotionModel);
    }

    @Override
    public boolean deletePromotionById(Long id) {
        requireId(id, "promotionId");
        if(promotionRepository.findById(id).isEmpty()){
            return false;
        }
        return promotionRepository.deleteById(id);
    }
}
