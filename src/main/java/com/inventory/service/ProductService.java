package com.inventory.service;

import com.inventory.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Product saveProduct(Product product);

    Optional<Product> getProductById(Long id);

    List<Product> getAllProducts();

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    // ⭐ EXTRA METHODS FOR DASHBOARD ⭐
    long getTotalProducts();

    long getLowStockCount();

    long getOutOfStockCount();
    
}
