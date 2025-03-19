package com.example.FinalProject.controller;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.service.LoyaltyProgramService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loyalty-programs")
@AllArgsConstructor
public class LoyaltyProgramController {

    private final LoyaltyProgramService loyaltyProgramService;

    @PostMapping("/create")
    public ResponseEntity<LoyaltyProgramDto> createLoyaltyProgram(@RequestBody LoyaltyProgramDto loyaltyProgramDto) {
        LoyaltyProgramDto createdLoyaltyProgram = loyaltyProgramService.createLoyaltyProgram(loyaltyProgramDto);
        return ResponseEntity.ok(createdLoyaltyProgram);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateLoyaltyProgram(@PathVariable UUID id, @RequestBody LoyaltyProgramDto loyaltyProgramDto) {
        loyaltyProgramService.updateLoyaltyProgram(id, loyaltyProgramDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoyaltyProgramDto> getLoyaltyProgramById(@PathVariable UUID id) {
        LoyaltyProgramDto loyaltyProgramDto = loyaltyProgramService.findLoyaltyProgramDtoById(id);
        return ResponseEntity.ok(loyaltyProgramDto);
    }

    @GetMapping
    public ResponseEntity<List<LoyaltyProgramDto>> getAllLoyaltyPrograms() {
        List<LoyaltyProgramDto> loyaltyPrograms = loyaltyProgramService.findAllDto();
        return ResponseEntity.ok(loyaltyPrograms);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLoyaltyProgram(@PathVariable UUID id) {
        loyaltyProgramService.deleteLoyaltyProgram(id);
        return ResponseEntity.noContent().build();
    }
}
