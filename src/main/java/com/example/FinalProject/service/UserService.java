package com.example.FinalProject.service;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.dto.UserProfileDto;
import com.example.FinalProject.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    void updateUser(UUID userId, UserDto userDto);

    void updateProfileUser(UUID userId, UserProfileDto userProfileDto);

    List<UserDto> findAllUsersWithRolesAndLoyaltyPrograms();

    void updateUserRole(String email, String roleName);

    void updateUserLoyaltyProgram(String email, String loyaltyProgramName);

    User findUserById(UUID userId);

    UserDto findUseDtoById(UUID userId);

    UserProfileDto findUseProfileDtoById(UUID userId);

    UUID findUserIdByUserEmail(String email);

    List<User> findAll();

    List<UserDto> findAllDto();

    void deleteUser(UUID userId);
}
