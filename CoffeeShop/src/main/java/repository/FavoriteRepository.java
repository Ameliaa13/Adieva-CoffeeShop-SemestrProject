package repository;

import model.FavoriteModel;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

    void addItem(Long userId, Long itemId);

    Optional<FavoriteModel> findByUserIdAndItemId(Long userId, Long itemId);

    List<FavoriteModel> findAllByUserId(Long id);

    boolean deleteItem(Long userId, Long itemId);

}
