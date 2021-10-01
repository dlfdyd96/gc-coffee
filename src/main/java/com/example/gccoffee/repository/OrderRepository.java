package com.example.gccoffee.repository;

import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {
    Order insert(Order order);

    Order update(Order order);

    List<Order> findAll();

    Optional<Order> findById(UUID orderId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);


}
