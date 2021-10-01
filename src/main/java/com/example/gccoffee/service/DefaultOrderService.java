package com.example.gccoffee.service;

import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import com.example.gccoffee.model.OrderStatus;
import com.example.gccoffee.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class DefaultOrderService implements OrderService {
    private static final Logger log = LoggerFactory.getLogger(DefaultOrderService.class);

    private final OrderRepository orderRepository;

    public DefaultOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems) {
        Order order = new Order(
                UUID.randomUUID(),
                email,
                address,
                postcode,
                orderItems,
                OrderStatus.ACCEPTED,
                LocalDateTime.now(),
                LocalDateTime.now());
        return orderRepository.insert(order);
    }

    @Override
    public List<Order> getOrderByOrderStatus(OrderStatus orderStatus) {
        return orderRepository.findByOrderStatus(orderStatus);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(UUID uuid) {
        log.info("get uuid: {}", uuid.toString());
        return orderRepository.findById(uuid);
    }

    @Override
    public Order updateOrder(UUID orderId, String email, String address, String postcode, OrderStatus orderStatus) {
        log.info("update uuid: {}", orderId.toString());
        var order = orderRepository.findById(orderId).map(
                item -> new Order(
                        orderId,
                        new Email(email),
                        address,
                        postcode,
                        item.getOrderItems(),
                        orderStatus,
                        item.getCreatedAt(),
                        LocalDateTime.now())
        ).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });

        return orderRepository.update(order);
    }
}
