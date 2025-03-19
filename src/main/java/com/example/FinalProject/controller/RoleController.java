package com.example.FinalProject.controller;

import com.example.FinalProject.dto.RoleDto;
import com.example.FinalProject.service.RoleService;
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
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto roleDto){
        RoleDto createdRole = roleService.createRole(roleDto);
        return ResponseEntity.ok(createdRole);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateRole(@PathVariable UUID id, @RequestBody RoleDto roleDto){
        roleService.updateRole(id, roleDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RoleDto>> getAllRoles(){
        List<RoleDto> allDto = roleService.findAllDto();
        return ResponseEntity.ok(allDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
