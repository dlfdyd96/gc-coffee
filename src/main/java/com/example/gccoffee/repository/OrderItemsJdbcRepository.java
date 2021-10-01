package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.OrderItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.example.gccoffee.JdbcUtils.toUUID;

@Repository
public class OrderItemsJdbcRepository implements OrderItemsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderItemsJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<OrderItem> findByOrderId(UUID orderId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                Collections.singletonMap("orderId", orderId.toString().getBytes()),
                orderItemRowMapper
        );
    }

    @Override
    public List<OrderItem> findByProductId(UUID productId) {
        return jdbcTemplate.query(
                "SELECT * FROM order_items WHERE product_id = UUID_TO_BIN(:productId)",
                Collections.singletonMap("productId", productId.toString().getBytes()),
                orderItemRowMapper
        );
    }

    public static final RowMapper<OrderItem> orderItemRowMapper = (resultSet, i) -> {
        var productId = toUUID(resultSet.getBytes("product_id"));
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");

        return new OrderItem(productId, category, price, quantity);
    };

}
