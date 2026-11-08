package service;

import model.FavoriteModel;

import java.util.List;
import java.util.Optional;

public interface FavoriteService {

    void addItem(Long userId, Long itemId);

    Optional<FavoriteModel> getFavoriteItem(Long userId, Long itemId);

    List<FavoriteModel> getAllFavorites(Long id);

    boolean deleteItem(Long userId, Long itemId);
}
