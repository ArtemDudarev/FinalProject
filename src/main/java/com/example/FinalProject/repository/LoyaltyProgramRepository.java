package com.example.FinalProject.repository;

import com.example.FinalProject.model.LoyaltyProgram;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoyaltyProgramRepository extends JpaRepository<LoyaltyProgram, UUID> {}
