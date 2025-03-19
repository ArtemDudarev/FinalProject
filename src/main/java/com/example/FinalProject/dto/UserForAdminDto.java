package com.example.FinalProject.dto;

import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserForAdminDto {
    private String email;
    private Role role;
    private LoyaltyProgram loyaltyProgram;
}
