package com.example.FinalProject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoyaltyProgramDto {
    private String programName;
    private String description;
    private Double discountPercentage;
}