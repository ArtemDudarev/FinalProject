package com.example.FinalProject.controller;

import com.example.FinalProject.dto.LoyaltyProgramDto;
import com.example.FinalProject.service.LoyaltyProgramService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoyaltyProgramControllerTest {

    @Mock
    private LoyaltyProgramService loyaltyProgramService;

    @InjectMocks
    private LoyaltyProgramController loyaltyProgramController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private LoyaltyProgramDto loyaltyProgramDto;
    private UUID loyaltyProgramId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loyaltyProgramController).build();
        objectMapper = new ObjectMapper();
        loyaltyProgramId = UUID.randomUUID();
        loyaltyProgramDto = LoyaltyProgramDto.builder().programName("Test Program").build();
    }

    @Test
    void testCreateLoyaltyProgram() throws Exception {
        when(loyaltyProgramService.createLoyaltyProgram(any(LoyaltyProgramDto.class))).thenReturn(loyaltyProgramDto);

        mockMvc.perform(post("/api/loyalty-programs/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(loyaltyProgramDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.programName").value("Test Program"));

        verify(loyaltyProgramService, times(1)).createLoyaltyProgram(any(LoyaltyProgramDto.class));
    }

    @Test
    void testUpdateLoyaltyProgram() throws Exception {
        doNothing().when(loyaltyProgramService).updateLoyaltyProgram(eq(loyaltyProgramId), any(LoyaltyProgramDto.class));

        mockMvc.perform(put("/api/loyalty-programs/update/{id}", loyaltyProgramId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(loyaltyProgramDto)))
               .andExpect(status().isNoContent());

        verify(loyaltyProgramService, times(1)).updateLoyaltyProgram(eq(loyaltyProgramId), any(LoyaltyProgramDto.class));
    }

    @Test
    void testGetLoyaltyProgramById() throws Exception {
        when(loyaltyProgramService.findLoyaltyProgramDtoById(loyaltyProgramId)).thenReturn(loyaltyProgramDto);

        mockMvc.perform(get("/api/loyalty-programs/{id}", loyaltyProgramId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.programName").value("Test Program"));

        verify(loyaltyProgramService, times(1)).findLoyaltyProgramDtoById(loyaltyProgramId);
    }

    @Test
    void testGetAllLoyaltyPrograms() throws Exception {
        List<LoyaltyProgramDto> loyaltyProgramDtos = Arrays.asList(loyaltyProgramDto, LoyaltyProgramDto.builder().programName("Another Program").build());
        when(loyaltyProgramService.findAllDto()).thenReturn(loyaltyProgramDtos);

        mockMvc.perform(get("/api/loyalty-programs")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].programName").value("Test Program"))
               .andExpect(jsonPath("$[1].programName").value("Another Program"));

        verify(loyaltyProgramService, times(1)).findAllDto();
    }

    @Test
    void testDeleteLoyaltyProgram() throws Exception {
        doNothing().when(loyaltyProgramService).deleteLoyaltyProgram(loyaltyProgramId);

        mockMvc.perform(delete("/api/loyalty-programs/delete/{id}", loyaltyProgramId))
               .andExpect(status().isNoContent());

        verify(loyaltyProgramService, times(1)).deleteLoyaltyProgram(loyaltyProgramId);
    }
}