package com.example.gccoffee.repository;

import com.example.gccoffee.model.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemsRepository {
    List<OrderItem> findByOrderId(UUID orderId);

    List<OrderItem> findByProductId(UUID productId);

}
