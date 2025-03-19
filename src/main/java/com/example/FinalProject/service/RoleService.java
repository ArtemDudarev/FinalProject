package com.example.FinalProject.service;

import com.example.FinalProject.dto.RoleDto;
import com.example.FinalProject.model.Role;
import java.util.List;
import java.util.UUID;

public interface RoleService {

    RoleDto createRole(RoleDto roleDto);

    void updateRole(UUID roleId, RoleDto roleDto);

    Role findRoleById(UUID roleId);

    RoleDto findRoleDtoById(UUID roleId);

    Role findRoleByName(String roleName);

    List<Role> findAll();

    List<RoleDto> findAllDto();

    void deleteRole(UUID roleId);
}
