package com.example.gccoffee.repository;

import com.example.gccoffee.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.gccoffee.JdbcUtils.toLocalDateTime;
import static com.example.gccoffee.JdbcUtils.toUUID;

@Repository
public class OrderJdbcRepository implements OrderRepository {
    private static final Logger log = LoggerFactory.getLogger(OrderJdbcRepository.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final OrderItemsJdbcRepository orderItemRepository;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate, OrderItemsJdbcRepository orderItemRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        " VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                        " VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return null;
    }

    @Override
    public Order update(Order order) {
        log.info("order : {}", order.toString());
        var update = jdbcTemplate.update(
                "UPDATE orders SET email = :email, address = :address, postcode = :postcode, order_status = :orderStatus, created_at = :createdAt, updated_at = :updatedAt" +
                " WHERE order_id = UUID_TO_BIN(:orderId)",
                toOrderParamMap(order));
        if (update != 1) {
            throw new RuntimeException("Nothing was updated"); // TODO: 적절한 예외처리 생각해보기
        }
        return order;
    }

    @Override
    @Transactional
    public List<Order> findAll() {
        var orders = jdbcTemplate.query("SELECT * FROM orders", orderRowMapper);
        orders.forEach(order -> {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getOrderId());
            order.setOrderItems(orderItems);
        });
        return orders;
    }

    @Override
    @Transactional
    public Optional<Order> findById(UUID orderId) {
        try {
            var order = jdbcTemplate.queryForObject(
                    "SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                    Collections.singletonMap("orderId", orderId.toString().getBytes()),
                    orderRowMapper);
            order.setOrderItems(orderItemRepository.findByOrderId(order.getOrderId()));
            return Optional.of(order);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Order> findByOrderStatus(OrderStatus orderStatus) {
        return jdbcTemplate.query(
                "SELECT * FROM orders WHERE order_status = :orderStatus",
                Collections.singletonMap("orderStatus", orderStatus.toString()),
                orderRowMapper
        );
    }

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }

    private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        // var orderItems = resultSet.get;
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
        var orderItems = new ArrayList<OrderItem>();

        return new Order(orderId, email, address, postcode, orderItems, orderStatus, createdAt, updatedAt);
    };
}
