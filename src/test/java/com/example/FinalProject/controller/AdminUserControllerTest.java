package com.example.FinalProject.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AdminUserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
        objectMapper = new ObjectMapper();
        userDto = UserDto.builder().email("test@example.com").firstName("testUser").build();
    }

    @Test
    void testGetAllUsersWithRolesAndLoyaltyPrograms() throws Exception {
        List<UserDto> userDtos = Arrays.asList(userDto, UserDto.builder().email("another@example.com").firstName("anotherUser").build());
        when(userService.findAllUsersWithRolesAndLoyaltyPrograms()).thenReturn(userDtos);

        mockMvc.perform(get("/api/admin/manage-users")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].email").value("test@example.com"))
               .andExpect(jsonPath("$[1].email").value("another@example.com"));

        verify(userService, times(1)).findAllUsersWithRolesAndLoyaltyPrograms();
    }

    @Test
    void testUpdateUserRole() throws Exception {
        doNothing().when(userService).updateUserRole("test@example.com", "ADMIN");

        mockMvc.perform(put("/api/admin/manage-users/role")
                   .param("email", "test@example.com")
                   .param("roleName", "ADMIN"))
               .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUserRole("test@example.com", "ADMIN");
    }

    @Test
    void testUpdateUserLoyaltyProgram() throws Exception {
        doNothing().when(userService).updateUserLoyaltyProgram("test@example.com", "GOLD");

        mockMvc.perform(put("/api/admin/manage-users/loyalty-program")
                   .param("email", "test@example.com")
                   .param("loyaltyProgramName", "GOLD"))
               .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUserLoyaltyProgram("test@example.com", "GOLD");
    }
}
