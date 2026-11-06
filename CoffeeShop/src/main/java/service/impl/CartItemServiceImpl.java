package service.impl;

import model.CartItemModel;
import repository.CartItemRepository;
import repository.MenuItemRepository;
import repository.impl.CartItemRepositoryImpl;
import repository.impl.MenuItemRepositoryImpl;
import service.CartItemService;
import utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class CartItemServiceImpl extends ValidationUtils<CartItemModel> implements CartItemService {

    private final CartItemRepository cartItemRepository = CartItemRepositoryImpl.getInstance();
    private final MenuItemRepository menuItemRepository = MenuItemRepositoryImpl.getInstance();

    private CartItemServiceImpl(){}

    private static class Holder{
        private static final CartItemServiceImpl INSTANCE = new CartItemServiceImpl();
    }
    public static CartItemServiceImpl getInstance(){
        return CartItemServiceImpl.Holder.INSTANCE;
    }


    @Override
    public void validate(CartItemModel model) {
        requireNonNull(model, "model");
        requireId(model.getUserId(), "userId");
        requireId(model.getMenuItemId(), "menuItemId");
        requirePositive(model.getQuantity(), "quantity");
    }

    private void requireMenuItemExists(Long menuItemId) {
        requireId(menuItemId, "menuItemId");

        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw error("menuItemId", "Блюдо не найдено.");
        }
    }

    @Override
    public void addItem(Long userId, Long itemId) {

        requireId(userId, "userId");
        requireMenuItemExists(itemId);
        cartItemRepository.save(userId, itemId);
    }

    @Override
    public Optional<CartItemModel> getItemByIds(Long userId, Long itemId) {

        requireId(userId, "userId");
        requireId(itemId, "itemId");
        return cartItemRepository.findByTwoId(userId, itemId);
    }

    @Override
    public List<CartItemModel> getAllItems(Long userId) {

        requireId(userId, "userId");
        return cartItemRepository.findAll(userId);
    }

    @Override
    public boolean updateQuantity(Long userId, Long itemId, Integer quantity) {

        CartItemModel model = CartItemModel.builder()
                .userId(userId)
                .menuItemId(itemId)
                .quantity(quantity)
                .build();

        validate(model);
        return cartItemRepository.updateQuantity(userId, itemId, quantity);
    }

    @Override
    public boolean deleteItem(Long userId, Long itemId) {

        requireId(userId, "userId");
        requireId(itemId, "itemId");
        return cartItemRepository.deleteByTwoId(userId, itemId);
    }

    @Override
    public boolean clearCart(Long userId) {

        requireId(userId, "userId");
        return cartItemRepository.clearByUserId(userId);
    }
}
