package com.inventory.controller;

import com.inventory.model.Order;
import com.inventory.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Place Order
    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long productId,
                            @RequestParam int quantity,
                            @RequestParam String orderedBy) {

        return orderService.placeOrder(productId, quantity, orderedBy);
    }

    // View All Orders
    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    // View Order by ID
    @GetMapping("/{id}")
    public Order getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id).orElse(null);
    }
}
