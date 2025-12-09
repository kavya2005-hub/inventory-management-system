package com.inventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "staff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Staff full name
    private String name;

    // Unique email ID
    @Column(unique = true)
    private String email;

    // Role inside the company
    private String designation;   // Manager, Cashier, Stock Handler...

    // Department inside the organisation
    private String department;    // Sales, Inventory, Admin...

    // Rights (admin/staff)
    private String rights;

    // Phone number of staff
    private String phoneNumber;

    // Active / Inactive
    private String status;         // ACTIVE or INACTIVE

    // Login password
    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
