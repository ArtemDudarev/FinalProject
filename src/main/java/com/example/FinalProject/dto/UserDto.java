package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
    private UUID roleId;
    private UUID addressId;
}
