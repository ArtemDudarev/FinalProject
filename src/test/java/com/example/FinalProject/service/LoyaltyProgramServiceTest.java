package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import com.example.FinalProject.service.imp.LoyaltyProgramServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class LoyaltyProgramServiceTest {

    @Mock
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @InjectMocks
    private LoyaltyProgramServiceImp loyaltyProgramService;

    private LoyaltyProgramDto loyaltyProgramDto;
    private UUID programId;

    @BeforeEach
    void setUp() {
        loyaltyProgramDto = LoyaltyProgramDto.builder().programName("Test Loyalty Program").build();
        programId = UUID.randomUUID();
    }

    @Test
    void testCreateLoyaltyProgramErrorMessage() {
        when(loyaltyProgramRepository.save(any(LoyaltyProgram.class))).thenThrow(new RuntimeException("Test exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loyaltyProgramService.createLoyaltyProgram(loyaltyProgramDto);
        });

        assertEquals("Ошибка при сохранении Test Loyalty Program программы лояльности", exception.getMessage());
    }

    @Test
    void testUpdateLoyaltyProgramErrorMessage() {
        when(loyaltyProgramRepository.existsById(programId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loyaltyProgramService.updateLoyaltyProgram(programId, loyaltyProgramDto);
        });

        assertEquals("Ошибка при обновлении Test Loyalty Program программы лояльности", exception.getMessage());
    }

    @Test
    void testFindLoyaltyProgramDtoByIdErrorMessage() {
        when(loyaltyProgramRepository.findById(programId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loyaltyProgramService.findLoyaltyProgramDtoById(programId);
        });

        assertEquals("Ошибка при получении программы лояльности Dto по ID: " + programId, exception.getMessage());
    }

    @Test
    void testFindAllLoyaltyProgramsErrorMessage() {
        when(loyaltyProgramRepository.findAll()).thenThrow(new RuntimeException("Test exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            loyaltyProgramService.findAllDto();
        });

        assertEquals("Ошибка при получении всех программ лояльности Dto", exception.getMessage());
    }

}