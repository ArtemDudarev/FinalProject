package com.example.FinalProject.repository;

import com.example.FinalProject.model.UserLoyaltyProgram;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoyaltyProgramRepository extends JpaRepository<UserLoyaltyProgram, UUID> {}
