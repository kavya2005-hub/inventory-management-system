package com.inventory.dto;

import com.inventory.model.Staff;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    
    private Long id;

    private String name;

    private String rights;
}
