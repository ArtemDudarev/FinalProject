package com.example.FinalProject.service;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.model.LoyaltyProgram;
import java.util.List;
import java.util.UUID;

public interface LoyaltyProgramService {

    LoyaltyProgramDto createLoyaltyProgram(LoyaltyProgramDto loyaltyProgramDto);

    void updateLoyaltyProgram(UUID loyaltyProgramId, LoyaltyProgramDto loyaltyProgramDto);

    LoyaltyProgram findLoyaltyProgramById(UUID loyaltyProgramId);

    LoyaltyProgramDto findLoyaltyProgramDtoById(UUID loyaltyProgramId);

    List<LoyaltyProgram> findAll();

    List<LoyaltyProgramDto> findAllDto();

    void deleteLoyaltyProgram(UUID loyaltyProgramId);
}
