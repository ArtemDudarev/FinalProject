package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.RoleDto;
import com.example.FinalProject.model.Role;

public class RoleMapper {

    public static Role toEntity(RoleDto roleDto) {
        return Role.builder()
                   .roleName(roleDto.getRoleName())
                   .build();
    }

    public static RoleDto toDto(Role role) {
        return RoleDto.builder()
                      .roleName(role.getRoleName())
                      .build();
    }
}
