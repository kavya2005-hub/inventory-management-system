package com.inventory.dto;

import lombok.Data;

@Data
public class StaffLoginRequest {
    private String email;
    private String password;
}
