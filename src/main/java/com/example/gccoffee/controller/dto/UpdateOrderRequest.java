package com.example.gccoffee.controller.dto;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.OrderStatus;

public record UpdateOrderRequest(
        String email,
        String address,
        String postcode,
        OrderStatus orderStatus) {
}
