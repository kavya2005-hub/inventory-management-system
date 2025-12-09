package com.inventory.service.impl;

import com.inventory.model.Staff;
import com.inventory.repository.StaffRepository;
import com.inventory.service.StaffService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    @Override
    public Staff createStaff(Staff staff) {
        staff.setCreatedAt(LocalDateTime.now());
        staff.setUpdatedAt(LocalDateTime.now());
        return staffRepository.save(staff);
    }

    @Override
    public Staff getStaffById(Long id) {
        return staffRepository.findById(id).orElse(null);
    }

    @Override
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @Override
    public Staff updateStaff(Long id, Staff staff) {
        Staff existing = staffRepository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setName(staff.getName());
        existing.setEmail(staff.getEmail());
        existing.setDesignation(staff.getDesignation());
        existing.setDepartment(staff.getDepartment());
        existing.setRights(staff.getRights());
        existing.setPhoneNumber(staff.getPhoneNumber());
        existing.setStatus(staff.getStatus());
        existing.setPassword(staff.getPassword());
        existing.setUpdatedAt(LocalDateTime.now());

        return staffRepository.save(existing);
    }

    @Override
    public void deleteStaff(Long id) {
        staffRepository.deleteById(id);
    }

    @Override
    public Staff findByEmail(String email) {
        return staffRepository.findByEmail(email);
    }

    @Override
    public List<Staff> getStaffByRights(String rights) {
        return staffRepository.findAll()
                .stream()
                .filter(s -> s.getRights() != null && s.getRights().equalsIgnoreCase(rights))
                .toList();
    }

    @Override
    public List<Staff> getStaffByStatus(String status) {
        return staffRepository.findAll()
                .stream()
                .filter(s -> s.getStatus() != null && s.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    // ⭐⭐⭐ THIS IS THE MISSING METHOD ⭐⭐⭐
    @Override
    public long getStaffCount() {
        return staffRepository.count();
    }
}
