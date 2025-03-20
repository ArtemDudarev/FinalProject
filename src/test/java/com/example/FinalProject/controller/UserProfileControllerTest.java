package com.example.FinalProject.controller;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.dto.UserProfileDto;
import com.example.FinalProject.service.UserService;
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
class UserProfileControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserProfileController userProfileController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserDto userDto;
    private UserProfileDto userProfileDto;
    private UUID userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userProfileController).build();
        objectMapper = new ObjectMapper();
        userId = UUID.randomUUID();
        userDto = UserDto.builder().email("test@example.com").firstName("testUser").build();
        userProfileDto = UserProfileDto.builder().firstName("Test").lastName("User").build();
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/profile/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(userDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(userService, times(1)).createUser(any(UserDto.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        doNothing().when(userService).updateProfileUser(eq(userId), any(UserProfileDto.class));

        mockMvc.perform(put("/api/profile/update/{id}", userId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(userProfileDto)))
               .andExpect(status().isNoContent());

        verify(userService, times(1)).updateProfileUser(eq(userId), any(UserProfileDto.class));
    }

    @Test
    void testGetUserById() throws Exception {
        when(userService.findUseProfileDtoById(userId)).thenReturn(userProfileDto);

        mockMvc.perform(get("/api/profile/getProfile/{id}", userId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.firstName").value("Test"));

        verify(userService, times(1)).findUseProfileDtoById(userId);
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> userDtos = Arrays.asList(userDto, UserDto.builder().email("another@example.com").firstName("anotherUser").build());
        when(userService.findAllDto()).thenReturn(userDtos);

        mockMvc.perform(get("/api/profile/users")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].email").value("test@example.com"))
               .andExpect(jsonPath("$[1].email").value("another@example.com"));

        verify(userService, times(1)).findAllDto();
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/profile/delete/{id}", userId))
               .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }
}
