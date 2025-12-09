package com.inventory.repository;

import com.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // For order API
    Product findByProductNameAndModel(String productName, String model);

    // For dashboard counts
    long countByQuantityLessThan(int qty);

    long countByQuantity(int qty);
}
