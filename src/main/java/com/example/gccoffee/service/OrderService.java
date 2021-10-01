package com.example.gccoffee.service;

import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Order;
import com.example.gccoffee.model.OrderItem;
import com.example.gccoffee.model.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {

    Order createOrder(Email email, String address, String postcode, List<OrderItem> orderItems);

    List<Order> getOrderByOrderStatus(OrderStatus orderStatus);

    List<Order> getAllOrders();

    Optional<Order> getOrderById(UUID uuid);

    Order updateOrder(UUID uuid, String email, String address, String postcode, OrderStatus orderStatus);
}