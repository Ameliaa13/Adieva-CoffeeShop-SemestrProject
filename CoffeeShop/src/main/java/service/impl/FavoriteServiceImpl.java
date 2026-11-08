package service.impl;

import model.FavoriteModel;
import repository.FavoriteRepository;
import repository.MenuItemRepository;
import repository.impl.FavoriteRepositoryImpl;
import repository.impl.MenuItemRepositoryImpl;
import service.FavoriteService;
import utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class FavoriteServiceImpl extends ValidationUtils<FavoriteModel> implements FavoriteService {

    private final FavoriteRepository favoriteRepository = FavoriteRepositoryImpl.getInstance();
    private final MenuItemRepository menuItemRepository = MenuItemRepositoryImpl.getInstance();

    private FavoriteServiceImpl(){}
    private static class Holder{
        private static final FavoriteServiceImpl INSTANCE = new FavoriteServiceImpl();
    }
    public static FavoriteServiceImpl getInstance(){
        return FavoriteServiceImpl.Holder.INSTANCE;
    }


    @Override
    public void validate(FavoriteModel favoriteModel) {
        requireNonNull(favoriteModel, "model");
        requireId(favoriteModel.getUserId(), "userId");
        requireId(favoriteModel.getMenuItemId(), "menuItemId");
    }

    private void requireMenuItemExists(Long menuItemId) {
        requireId(menuItemId, "menuItemId");

        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw error("menuItemId", "Блюдо не найдено.");
        }
    }

    @Override
    public void addItem(Long userId, Long itemId) {
        validate(FavoriteModel.builder()
                .userId(userId)
                .menuItemId(itemId)
                .build());
        requireMenuItemExists(itemId);

        favoriteRepository.save(userId, itemId);
    }

    @Override
    public Optional<FavoriteModel> getFavoriteItem(Long userId, Long itemId) {
        requireId(userId, "userId");
        requireId(itemId, "itemId");

        return favoriteRepository.findByTwoId(userId, itemId);
    }

    @Override
    public List<FavoriteModel> getAllFavorites(Long userId) {
        requireId(userId, "userId");
        return favoriteRepository.findAll(userId);
    }

    @Override
    public boolean deleteItem(Long userId, Long itemId) {
        requireId(userId, "userId");
        requireId(itemId, "itemId");
        return favoriteRepository.deleteByTwoId(userId, itemId);
    }
}
