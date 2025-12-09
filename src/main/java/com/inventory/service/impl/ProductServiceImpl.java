package com.inventory.service.impl;

import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import com.inventory.service.ProductService;
import com.inventory.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    private final int minimumStock = 4;  // Low stock threshold

    @Override
    public Product saveProduct(Product product) {

        // Save first into DB
        Product saved = productRepository.save(product);

        // If stock is low → send alert ONCE
        if (saved.getQuantity() != null && saved.getQuantity() < minimumStock) {
            notificationService.sendStockAlert(saved);
        }

        // If stock is normal (>=4) → reset mail flag
        if (saved.getQuantity() != null && saved.getQuantity() >= minimumStock) {
            notificationService.clearMailedFlag(saved.getId());
        }

        return saved;
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product updateProduct(Long id, Product newProduct) {

        return productRepository.findById(id).map(existing -> {

            // Update product fields
            existing.setProductCode(newProduct.getProductCode());
            existing.setProductName(newProduct.getProductName());
            existing.setModel(newProduct.getModel());
            existing.setPricePerQuantity(newProduct.getPricePerQuantity());
            existing.setQuantity(newProduct.getQuantity());
            existing.setTotalPrice(newProduct.getPricePerQuantity() * newProduct.getQuantity());
            existing.setStatus(newProduct.getStatus());
            existing.setUpdatedDate(LocalDateTime.now());

            Product updated = productRepository.save(existing);

            // If quantity becomes normal → reset mail flag
            if (updated.getQuantity() != null && updated.getQuantity() >= minimumStock) {
                notificationService.clearMailedFlag(updated.getId());
            }

            // If quantity < 4 → send low stock mail ONCE
            if (updated.getQuantity() != null && updated.getQuantity() < minimumStock) {
                notificationService.sendStockAlert(updated);
            }

            return updated;

        }).orElse(null);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public long getTotalProducts() {
        return productRepository.count();
    }

    @Override
    public long getLowStockCount() {
        return productRepository.countByQuantityLessThan(10);
    }

    @Override
    public long getOutOfStockCount() {
        return productRepository.countByQuantity(0);
    }
}
