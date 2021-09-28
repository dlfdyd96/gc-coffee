package com.example.gccoffee.controller;

import com.example.gccoffee.controller.dto.CreateProductRequest;
import com.example.gccoffee.controller.dto.UpdateProductRequest;
import com.example.gccoffee.model.Category;
import com.example.gccoffee.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Controller
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String productPage(@RequestParam Optional<Category> category, Model model) {
        var products = category
                .map(productService::getProductByCategory)
                .orElse(productService.getAllProducts());
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("new-product")
    public String newProduct() {
        return "new-product";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") UUID uuid, Model model) {
        var product = productService.getProductById(uuid).get(); // TODO: not found 예외 처리
        model.addAttribute("product", product);
        return "product-detail";
    }

    @PostMapping("/products")
    public String newProduct(CreateProductRequest createProductRequest) {
        productService.createProduct(createProductRequest.productName(),
                createProductRequest.category(),
                createProductRequest.price(),
                createProductRequest.description());
        return "redirect:/products";
    }

    @PutMapping("/products/{id}")
    public String updateProduct(@PathVariable("id") UUID uuid,
                                UpdateProductRequest updateProductRequest) {
        log.info("update uuid: {}", uuid);
        productService.updateProduct(
                uuid,
                updateProductRequest.productName(),
                updateProductRequest.category(),
                updateProductRequest.price(),
                updateProductRequest.description());
        return "redirect:/products";
    }
}
