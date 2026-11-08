package repository;

import model.CartItemModel;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends CrudTwoParametersRepository<CartItemModel>{

    boolean updateQuantity(Long userId, Long itemId, Integer quantity);

    boolean clearByUserId(Long userId);

}
