package service;

import model.CartItemModel;

import java.util.List;
import java.util.Optional;

public interface CartItemService {

    void addItem(Long userId, Long cartItemId);

    Optional<CartItemModel> getItemByIds(Long userId, Long itemId);

    List<CartItemModel> getAllItems(Long id);

    boolean updateQuantity(Long userId, Long itemId, Integer quantity);

    boolean deleteItem(Long userId, Long itemId);

    boolean clearCart(Long userId);
}
