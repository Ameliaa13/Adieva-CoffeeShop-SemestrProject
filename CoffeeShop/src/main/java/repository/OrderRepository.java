package repository;

import model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Long> createFromCart(Long userId);

    List<OrderModel> findAllByUserId(Long userId);

    Optional<OrderModel> findByIdAndUserId(Long orderId, Long userId);
}