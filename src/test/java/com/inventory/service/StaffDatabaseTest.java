package com.inventory.service;

import com.inventory.model.Staff;
import com.inventory.repository.StaffRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StaffDatabaseTest {

    @Autowired
    private StaffRepository staffRepository;

    @Test
    void testReadAllStaffFromDatabase() {
        List<Staff> allStaff = staffRepository.findAll();
        assertNotNull(allStaff, "Staff list should not be null");

        if (allStaff.isEmpty()) {
            System.out.println(" No staff found in the database. Please add records via API first.");
        } else {
            System.out.println(" Found " + allStaff.size() + " staff record(s) in the database.");
            allStaff.forEach(staff -> {
                System.out.println("ID: " + staff.getId() +
                        ", Name: " + staff.getName() +
                        ", Email: " + staff.getEmail() +
                        ", Status: " + staff.getStatus());
                assertNotNull(staff.getName(), "Staff name should not be null");
                assertNotNull(staff.getEmail(), "Staff email should not be null");
            });
        }
    }
}
