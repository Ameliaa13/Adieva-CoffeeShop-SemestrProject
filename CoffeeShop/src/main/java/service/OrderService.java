package service;

import model.OrderModel;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Long checkout(Long userId);

    Optional<OrderModel> getUserOrder(Long userId, Long orderId);

    List<OrderModel> getUserOrders(Long userId);
}