package com.inventory.service.impl;

import com.inventory.model.Order;
import com.inventory.model.Product;
import com.inventory.repository.OrderRepository;
import com.inventory.repository.ProductRepository;
import com.inventory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Order placeOrder(Long productId, int quantity, String orderedBy) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock available");
        }

        // Reduce stock
        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);

        // Save order
        Order order = new Order();
        order.setProductName(product.getProductName());
        order.setModel(product.getModel());
        order.setQuantityOrdered(quantity);
        order.setOrderedBy(orderedBy);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public long getTotalOrders() {
        return orderRepository.count();
    }
}
