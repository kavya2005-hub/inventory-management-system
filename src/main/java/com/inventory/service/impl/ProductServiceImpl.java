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

        // Save product to DB
        Product saved = productRepository.save(product);

        // If stock < threshold AND mail not sent, send email async
        if (saved.getQuantity() != null && saved.getQuantity() < minimumStock 
                && !Boolean.TRUE.equals(saved.getMailSent())) {

            notificationService.sendStockAlert(saved); // async call

            // mark mail as sent
            saved.setMailSent(true);
            productRepository.save(saved);
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

            existing.setProductCode(newProduct.getProductCode());
            existing.setProductName(newProduct.getProductName());
            existing.setModel(newProduct.getModel());
            existing.setPricePerQuantity(newProduct.getPricePerQuantity());
            existing.setQuantity(newProduct.getQuantity());
            existing.setTotalPrice(newProduct.getPricePerQuantity() * newProduct.getQuantity());
            existing.setStatus(newProduct.getStatus());
            existing.setUpdatedDate(LocalDateTime.now());

            Product updated = productRepository.save(existing);

            // If low stock AND mail not sent → async email
            if (updated.getQuantity() != null && updated.getQuantity() < minimumStock 
                    && !Boolean.TRUE.equals(updated.getMailSent())) {

                notificationService.sendStockAlert(updated); // async
                updated.setMailSent(true);
                productRepository.save(updated);
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
        return productRepository.countByQuantityLessThan(minimumStock);
    }

    @Override
    public long getOutOfStockCount() {
        return productRepository.countByQuantity(0);
    }
}
