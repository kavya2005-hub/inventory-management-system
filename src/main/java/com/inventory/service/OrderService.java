package com.inventory.service;

import com.inventory.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    Order placeOrder(Long productId, int quantity, String orderedBy);

    List<Order> getAllOrders();

    Optional<Order> getOrderById(Long id);

    long getTotalOrders();
}
