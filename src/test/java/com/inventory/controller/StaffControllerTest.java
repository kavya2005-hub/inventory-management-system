package com.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventory.model.Staff;
import com.inventory.repository.StaffRepository;
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
@Transactional
public class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StaffRepository staffRepository;

    // GET all staff
    @Test
    void testGetAllStaff() throws Exception {
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    // GET staff by ID
    @Test
    void testGetStaffById() throws Exception {
        Long existingId = staffRepository.findAll().stream()
                .findFirst()
                .map(Staff::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No staff found in DB to test GET by ID.");
            return;
        }

        mockMvc.perform(get("/api/staff/{id}", existingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId));
    }

    // POST create staff
    @Test
    void testAddStaff() throws Exception {

        Staff staff = new Staff();
        staff.setName("JUnit Staff");
        staff.setEmail("stafftest@gmail.com");
        staff.setDesignation("Manager");
        staff.setDepartment("Production");
        staff.setRights("staff");  // NOT enum
        staff.setPhoneNumber("9998877766");
        staff.setStatus("ACTIVE"); // NOT enum

        mockMvc.perform(post("/api/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(staff)))
                .andExpect(status().isOk())   // Controller returns 200 OK
                .andExpect(jsonPath("$.name").value("JUnit Staff"));
    }

    // PUT update staff
    @Test
    void testUpdateStaff() throws Exception {
        Long existingId = staffRepository.findAll().stream()
                .findFirst()
                .map(Staff::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No staff found in DB to test update.");
            return;
        }

        Optional<Staff> optionalStaff = staffRepository.findById(existingId);
        Staff staff = optionalStaff.get();
        staff.setDesignation("Senior Manager");

        mockMvc.perform(put("/api/staff/{id}", existingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(staff)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.designation").value("Senior Manager"));
    }

    // DELETE staff
    @Test
    void testDeleteStaff() throws Exception {
        Long existingId = staffRepository.findAll().stream()
                .findFirst()
                .map(Staff::getId)
                .orElse(null);

        if (existingId == null) {
            System.out.println("⚠ No staff found in DB to test delete.");
            return;
        }

        mockMvc.perform(delete("/api/staff/{id}", existingId))
                .andExpect(status().isNoContent());
    }
}
