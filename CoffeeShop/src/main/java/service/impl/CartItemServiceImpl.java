package service.impl;

import model.CartItemModel;
import repository.CartItemRepository;
import repository.impl.CartItemRepositoryImpl;
import service.CartItemService;

import java.util.List;
import java.util.Optional;

public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository = CartItemRepositoryImpl.getInstance();

    private CartItemServiceImpl(){}
    private static class Holder{
        private static final CartItemServiceImpl INSTANCE = new CartItemServiceImpl();
    }
    public static CartItemServiceImpl getInstance(){
        return CartItemServiceImpl.Holder.INSTANCE;
    }

    @Override
    public void addItem(Long userId, Long cartItemId) {
        cartItemRepository.addItem(userId, cartItemId);
    }

    @Override
    public Optional<CartItemModel> getItemByIds(Long userId, Long itemId) {
        return cartItemRepository.findByUserIdAndItemId(userId, itemId);
    }

    @Override
    public List<CartItemModel> getAllItems(Long id) {
        return cartItemRepository.findAllByUserId(id);
    }

    @Override
    public boolean updateQuantity(Long userId, Long itemId, Integer quantity) {
        return cartItemRepository.updateQuantity(userId, itemId, quantity);
    }

    @Override
    public boolean deleteItem(Long userId, Long itemId) {
        return cartItemRepository.deleteItem(userId, itemId);
    }

    @Override
    public boolean clearCart(Long userId) {
        return cartItemRepository.clearByUserId(userId);
    }
}
