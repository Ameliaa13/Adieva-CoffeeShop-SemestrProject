package service.impl;

import model.OrderModel;
import repository.OrderRepository;
import repository.impl.OrderRepositoryImpl;
import service.OrderService;
import utils.ValidationUtils;

import java.util.List;
import java.util.Optional;

public class OrderServiceImpl extends ValidationUtils<OrderModel> implements OrderService {

    private final OrderRepository orderRepository = OrderRepositoryImpl.getInstance();
    private OrderServiceImpl() {}
    private static class Holder {
        private static final OrderServiceImpl INSTANCE = new OrderServiceImpl();
    }
    public static OrderServiceImpl getInstance() {
        return Holder.INSTANCE;
    }


    @Override
    public void validate(OrderModel order) {
        requireNonNull(order, "order");
        requireId(order.getUserId(), "userId");
        requireNonNegative(order.getTotalPrice(), "totalPrice");
        requireNonNull(order.getItems(), "items");
    }

    @Override
    public Long checkout(Long userId) {
        requireId(userId, "userId");

        return orderRepository.createFromCart(userId)
                .orElseThrow(() -> error("cart", "Корзина пуста, оформлять нечего."));
    }

    @Override
    public Optional<OrderModel> getUserOrder(Long userId, Long orderId) {
        requireId(userId, "userId");
        requireId(orderId, "orderId");

        return orderRepository.findByIdAndUserId(orderId, userId);
    }

    @Override
    public List<OrderModel> getUserOrders(Long userId) {
        requireId(userId, "userId");

        return orderRepository.findAllByUserId(userId);
    }
}