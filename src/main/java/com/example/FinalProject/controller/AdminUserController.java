package com.example.FinalProject.controller;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.service.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/manage-users")
@AllArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsersWithRolesAndLoyaltyPrograms() {
        List<UserDto> users = userService.findAllUsersWithRolesAndLoyaltyPrograms();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/role")
    public ResponseEntity<Void> updateUserRole(@RequestParam String email, @RequestParam String roleName) {
        userService.updateUserRole(email, roleName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/loyalty-program")
    public ResponseEntity<Void> updateUserLoyaltyProgram(@RequestParam String email, @RequestParam String loyaltyProgramName) {
        userService.updateUserLoyaltyProgram(email, loyaltyProgramName);
        return ResponseEntity.noContent().build();
    }
}
