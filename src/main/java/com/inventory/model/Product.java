package com.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key

   @Column(nullable = true)
private String productCode;
  // 🔹 Added new field to identify product uniquely (ex: motorola)

    @Column(nullable = false)
    private String productName;  // Product name

    @Column(nullable = false)
    private String model;  // Model name

    @Column(nullable = false)
    private Double pricePerQuantity;  // Price per unit

    @Column(nullable = false)
    private Integer quantity;  // Quantity in stock

    @Column(nullable = false)
    private Double totalPrice;  // pricePerQuantity * quantity

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private String status;  // ACTIVE / INACTIVE

    @PrePersist
    public void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
        if (totalPrice == null && pricePerQuantity != null && quantity != null) {
            totalPrice = pricePerQuantity * quantity;
        }
    }

    @PreUpdate
    public void onUpdate() {
        updatedDate = LocalDateTime.now();
        if (pricePerQuantity != null && quantity != null) {
            totalPrice = pricePerQuantity * quantity;
        }
    }
}
