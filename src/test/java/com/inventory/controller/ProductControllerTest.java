package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.Product;
import com.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Rolls back after each test (no real data changes)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    // 🔹 Test: GET all products
    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // 🔹 Test: GET product by ID
    @Test
    void testGetProductById() throws Exception {
        Long existingId = productRepository.findAll().stream()
                .findFirst()
                .map(Product::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No product found in DB to test GET by ID.");
            return;
        }

        mockMvc.perform(get("/api/products/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // 🔹 Test: POST Add new product
    @Test
    void testAddProduct() throws Exception {

        Product product = new Product();
        product.setProductCode("TEST123");   // 🔥 FIX: Required for DB
        product.setProductName("JUnit Test Product");
        product.setModel("HP Pro");
        product.setPricePerQuantity(55000.0);
        product.setQuantity(5);
        product.setStatus("ACTIVE");

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated()) // 201 Created
                .andExpect(jsonPath("$.productName").value("JUnit Test Product"));
    }

    // 🔹 Test: PUT Update product
    @Test
    void testUpdateProduct() throws Exception {
        Long existingId = productRepository.findAll().stream()
                .findFirst()
                .map(Product::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No product found in DB to test UPDATE.");
            return;
        }

        Optional<Product> optionalProduct = productRepository.findById(existingId);
        Product product = optionalProduct.get();

        product.setQuantity(product.getQuantity() + 2); // Increase by 2

        mockMvc.perform(put("/api/products/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());
    }

    // 🔹 Test: DELETE product
    @Test
    void testDeleteProduct() throws Exception {
        Long existingId = productRepository.findAll().stream()
                .findFirst()
                .map(Product::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No product found in DB to test DELETE.");
            return;
        }

        mockMvc.perform(delete("/api/products/{id}", existingId))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}
