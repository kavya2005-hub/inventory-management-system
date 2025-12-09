package com.inventory.service;

import com.inventory.model.Staff;
import java.util.List;

public interface StaffService {

    Staff createStaff(Staff staff);

    Staff getStaffById(Long id);

    List<Staff> getAllStaff();

    Staff updateStaff(Long id, Staff staff);

    void deleteStaff(Long id);

    Staff findByEmail(String email);

    List<Staff> getStaffByRights(String rights);

    List<Staff> getStaffByStatus(String status);

    long getStaffCount();   // ✔ only signature
}
