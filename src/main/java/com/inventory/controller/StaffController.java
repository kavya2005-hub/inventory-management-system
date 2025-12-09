package com.inventory.controller;

import com.inventory.dto.LoginResponse;
import com.inventory.dto.StaffLoginRequest;
import com.inventory.model.Staff;
import com.inventory.repository.StaffRepository;
import com.inventory.service.StaffService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/staff")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @Autowired
    private StaffRepository staffRepository;

    // Create Staff
    @PostMapping
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.createStaff(staff));
    }

    // Get Staff by ID
    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaff(@PathVariable Long id) {
        return ResponseEntity.ok(staffService.getStaffById(id));
    }

    // Get all Staff
    @GetMapping
    public ResponseEntity<List<Staff>> getAllStaff() {
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    // Update Staff
    @PutMapping("/{id}")
    public ResponseEntity<Staff> updateStaff(@PathVariable Long id, @RequestBody Staff staff) {
        return ResponseEntity.ok(staffService.updateStaff(id, staff));
    }

    // Delete Staff
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStaff(@PathVariable Long id) {
        staffService.deleteStaff(id);
        return ResponseEntity.noContent().build();
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody StaffLoginRequest request) {

        Staff staff = staffService.findByEmail(request.getEmail());

        if (staff == null) {
            return ResponseEntity.status(401).body("Invalid Email!");
        }

        if (!staff.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(401).body("Invalid Password!");
        }

        if (!"ACTIVE".equalsIgnoreCase(staff.getStatus())) {
            return ResponseEntity.status(403).body("Your account is inactive. Contact admin.");
        }

        return ResponseEntity.ok(
                new LoginResponse(
                        staff.getId(),
                        staff.getName(),
                        staff.getRights().toLowerCase()
                )
        );
    }
    @GetMapping("/count")
public long getTotalStaff() {
    return staffRepository.count();
}

@GetMapping("/count/admin")
public long getAdminCount() {
    return staffRepository.countByRightsIgnoreCase("ADMIN");
}

@GetMapping("/count/staff")
public long getStaffCount() {
    return staffRepository.countByRightsIgnoreCase("STAFF");
}

@GetMapping("/count/new")
public long getNewStaffCount() {
    LocalDate lastWeek = LocalDate.now().minusDays(7);
    return staffRepository.countByCreatedAtAfter(lastWeek);
}

    

}
