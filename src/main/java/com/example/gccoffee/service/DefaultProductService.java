package com.example.gccoffee.service;

import com.example.gccoffee.controller.ProductController;
import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import com.example.gccoffee.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DefaultProductService implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(DefaultProductService.class);


    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProductByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String productName, Category category, long price) {
        var product = new Product(UUID.randomUUID(), productName, category, price);
        return productRepository.insert(product);
    }

    @Override
    public Product createProduct(String productName, Category category, long price, String description) {
        var product = new Product(UUID.randomUUID(), productName, category, price, description, LocalDateTime.now(), LocalDateTime.now());
        return productRepository.insert(product);
    }

    @Override
    public Optional<Product> getProductById(UUID uuid) {
        log.info("get uuid: {}", uuid.toString());
        return productRepository.findById(uuid);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID productId, String productName, Category category, long price, String description) {
        log.info("update uuid: {}", productId.toString());
        var product = productRepository.findById(productId).map(
                item -> {
                    item.setDescription(description);
                    item.setProductName(productName);
                    item.setPrice(price);
                    item.setCategory(category);
                    return item;
                }
        ).orElseThrow(() -> {
            throw new IllegalArgumentException();
        });

        var result = productRepository.update(product);

        return result;
    }

    @Override
    @Transactional
    public void deleteProduct(UUID productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException(
                            MessageFormat.format("Can't find product id : {}", productId.toString())
                    );
                });
        productRepository.deleteById(productId);
    }
}
