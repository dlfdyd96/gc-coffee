package com.example.gccoffee.service;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {

    List<Product> getProductByCategory(Category category);

    List<Product> getAllProducts();

    Product createProduct(String productName, Category category, long price);

    Product createProduct(String productName, Category category, long price, String description);

    Optional<Product> getProductById(UUID uuid);

    Product updateProduct(UUID productId, String productName, Category category, long price, String description);

    void deleteProduct(UUID productId);

}
