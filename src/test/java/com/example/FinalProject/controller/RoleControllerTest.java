package com.example.FinalProject.controller;

import com.example.FinalProject.dto.RoleDto;
import com.example.FinalProject.service.RoleService;
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
class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private RoleDto roleDto;
    private UUID roleId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper = new ObjectMapper();
        roleId = UUID.randomUUID();
        roleDto = RoleDto.builder().roleName("ROLE_TEST").build();
    }

    @Test
    void testCreateRole() throws Exception {
        when(roleService.createRole(any(RoleDto.class))).thenReturn(roleDto);

        mockMvc.perform(post("/api/roles/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(roleDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.roleName").value("ROLE_TEST"));

        verify(roleService, times(1)).createRole(any(RoleDto.class));
    }

    @Test
    void testUpdateRole() throws Exception {
        doNothing().when(roleService).updateRole(eq(roleId), any(RoleDto.class));

        mockMvc.perform(put("/api/roles/update/{id}", roleId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(roleDto)))
               .andExpect(status().isNoContent());

        verify(roleService, times(1)).updateRole(eq(roleId), any(RoleDto.class));
    }

    @Test
    void testGetAllRoles() throws Exception {
        List<RoleDto> roleDtos = Arrays.asList(roleDto, RoleDto.builder().roleName("ROLE_ADMIN").build());
        when(roleService.findAllDto()).thenReturn(roleDtos);

        mockMvc.perform(get("/api/roles/findAll")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].roleName").value("ROLE_TEST"))
               .andExpect(jsonPath("$[1].roleName").value("ROLE_ADMIN"));

        verify(roleService, times(1)).findAllDto();
    }

    @Test
    void testDeleteRole() throws Exception {
        doNothing().when(roleService).deleteRole(roleId);

        mockMvc.perform(delete("/api/roles/delete/{id}", roleId))
               .andExpect(status().isNoContent());

        verify(roleService, times(1)).deleteRole(roleId);
    }
}