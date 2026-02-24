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
    private Long id;

    @Column(nullable = true)
    private String productCode;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Double pricePerQuantity;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double totalPrice;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private String status;

    // ✅ ADD THIS FIELD (VERY IMPORTANT)
    @Column(name = "mail_sent")
    private Boolean mailSent = false;

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

        if (mailSent == null) {
            mailSent = false;
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
