package com.inventory.repository;

import com.inventory.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Login
    Staff findByEmail(String email);

    // Duplicate check
    boolean existsByEmail(String email);

    // Filter by rights (ADMIN / STAFF)
    List<Staff> findByRights(String rights);

    // Recommended (case insensitive)
    List<Staff> findByRightsIgnoreCase(String rights);

    // Filter by status
    List<Staff> findByStatus(String status);

    // Count total staff
    @Query("SELECT COUNT(s) FROM Staff s")
    long getStaffCount();

    long countByRightsIgnoreCase(String rights);

    long countByCreatedAtAfter(LocalDate date);
}
