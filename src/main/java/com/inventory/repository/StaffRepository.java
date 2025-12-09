package com.inventory.repository;

import com.inventory.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {

    // Login purpose
    Staff findByEmail(String email);

    // Check duplicate email during registration
    boolean existsByEmail(String email);

    // Filter by role (admin / staff)
    List<Staff> findByRights(String rights);

    // Filter by status (ACTIVE / INACTIVE)
    List<Staff> findByStatus(String status);

    // Count total staff
    @Query("SELECT COUNT(s) FROM Staff s")
    long getStaffCount();
    long countByRightsIgnoreCase(String rights);

long countByCreatedAtAfter(LocalDate date);

}
