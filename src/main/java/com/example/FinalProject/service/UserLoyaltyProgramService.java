package com.example.FinalProject.service;

import com.example.FinalProject.model.UserLoyaltyProgram;
import java.util.List;
import java.util.UUID;

public interface UserLoyaltyProgramService {

    void createUserLoyaltyProgram(UserLoyaltyProgram userLoyaltyProgram);

    void updateUserLoyaltyProgram(UserLoyaltyProgram userLoyaltyProgram);

    UserLoyaltyProgram findUserLoyaltyProgramById(UUID userLoyaltyProgramId);

    List<UserLoyaltyProgram> findAll();

    void deleteUserLoyaltyProgram(UUID userLoyaltyProgramId);
}
