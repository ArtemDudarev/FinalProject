package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.model.LoyaltyProgram;

public class LoyaltyProgramMapper {

    public static LoyaltyProgram toEntity(LoyaltyProgramDto loyaltyProgramDto) {
        return LoyaltyProgram.builder()
                             .programName(loyaltyProgramDto.getProgramName())
                             .description(loyaltyProgramDto.getDescription())
                             .discountPercentage(loyaltyProgramDto.getDiscountPercentage())
                             .build();
    }

    public static LoyaltyProgramDto toDto(LoyaltyProgram loyaltyProgram) {
        return LoyaltyProgramDto.builder()
                                .programName(loyaltyProgram.getProgramName())
                                .description(loyaltyProgram.getDescription())
                                .discountPercentage(loyaltyProgram.getDiscountPercentage())
                                .build();
    }
}
