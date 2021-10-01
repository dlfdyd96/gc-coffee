package com.example.gccoffee.controller;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.UpdateOrderRequest;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.OrderStatus;
import com.example.gccoffee.service.OrderService;
import com.example.gccoffee.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.UUID;

@Controller
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String orderPage(@RequestParam Optional<OrderStatus> orderStatus, Model model) {
        var orders = orderStatus
                .map(orderService::getOrderByOrderStatus)
                .orElse(orderService.getAllOrders());
        model.addAttribute("orders", orders);
        return "order-list";
    }

//    @GetMapping("new-product")
//    public String newProduct() {
//        return "new-product";
//    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable("id") UUID uuid, Model model) {
        orderService.getOrderById(uuid).ifPresentOrElse(order -> {
            model.addAttribute("order", order);
        }, () -> {
            throw new IllegalArgumentException(
                    MessageFormat.format("Can't find '{}' order ", uuid.toString())
            );
        });
        return "order-detail";
    }
//
//    @PostMapping("/orders")
//    public String newProduct(CreateProductRequest createProductRequest) {
//        orderService.createProduct(createProductRequest.productName(),
//                createProductRequest.category(),
//                createProductRequest.price(),
//                createProductRequest.description());
//        return "redirect:/orders";
//    }
//
    @PutMapping("/orders/{id}")
    public String updateOrder(@PathVariable("id") UUID uuid,
                                UpdateOrderRequest updateOrderRequest) {
        log.info("update uuid: {}", uuid);
        orderService.updateOrder(
                uuid,
                updateOrderRequest.email(),
                updateOrderRequest.address(),
                updateOrderRequest.postcode(),
                updateOrderRequest.orderStatus());

        return "redirect:/orders";
    }
//
//    @DeleteMapping("/orders/{id}")
//    public String deleteProduct(@PathVariable("id") UUID uuid) { // TODO: Not Found Exception?
//        log.info("delete uuid: {}", uuid);
//        orderService.deleteProduct(uuid);
//        return "redirect:/orders";
//    }
}
