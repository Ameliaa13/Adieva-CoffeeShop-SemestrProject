package repository;

import model.CartItemModel;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    void addItem(Long userId, Long cartItemId);

    Optional<CartItemModel> findByUserIdAndItemId(Long userId, Long itemId);

    List<CartItemModel> findAllByUserId(Long id);

    boolean updateQuantity(Long userId, Long itemId, Integer quantity);

    boolean deleteItem(Long userId, Long itemId);

    boolean clearByUserId(Long userId);

}
