package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoyaltyProgramDto {
    private UUID userId;
    private UUID loyaltyProgramId;
}