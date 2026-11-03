package service.impl;

import model.FavoriteModel;
import repository.FavoriteRepository;
import repository.impl.FavoriteRepositoryImpl;
import service.FavoriteService;

import java.util.List;
import java.util.Optional;

public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository = FavoriteRepositoryImpl.getInstance();

    private FavoriteServiceImpl(){}
    private static class Holder{
        private static final FavoriteServiceImpl INSTANCE = new FavoriteServiceImpl();
    }
    public static FavoriteServiceImpl getInstance(){
        return FavoriteServiceImpl.Holder.INSTANCE;
    }

    @Override
    public void addItem(Long userId, Long itemId) {
        favoriteRepository.addItem(userId, itemId);
    }

    @Override
    public Optional<FavoriteModel> getFavoriteItem(Long userId, Long itemId) {
        return favoriteRepository.findByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<FavoriteModel> getAllFavorites(Long id) {
        return favoriteRepository.findAllByUserId(id);
    }

    @Override
    public boolean deleteItem(Long userId, Long itemId) {
        return favoriteRepository.deleteItem(userId, itemId);
    }
}
