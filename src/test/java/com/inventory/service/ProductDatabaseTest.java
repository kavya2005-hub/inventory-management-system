package com.inventory.service;

import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductDatabaseTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void testReadAllProductsFromDatabase() {
        // Fetch all products from the database
        List<Product> allProducts = productRepository.findAll();
        assertNotNull(allProducts, "Product list should not be null");

        if (allProducts.isEmpty()) {
            System.out.println(" No products found in the database. Please add records via API first.");
        } else {
            System.out.println(" Found " + allProducts.size() + " product record(s) in the database.");
            allProducts.forEach(product -> {
                System.out.println(
                        "ID: " + product.getId() +
                        ", Name: " + product.getProductName() +
                        ", Model: " + product.getModel() +
                        ", Price/Qty: " + product.getPricePerQuantity() +
                        ", Quantity: " + product.getQuantity() +
                        ", Total: " + product.getTotalPrice() +
                        ", Status: " + product.getStatus()
                );

                // Assertions to validate product data integrity
                assertNotNull(product.getProductName(), "Product name should not be null");
                assertNotNull(product.getModel(), "Product model should not be null");
                assertTrue(product.getPricePerQuantity() >= 0, "Product price should not be negative");
                assertTrue(product.getQuantity() >= 0, "Product quantity should not be negative");
                assertNotNull(product.getStatus(), "Product status should not be null");
            });
        }
    }
}
