package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.UserLoyaltyProgramDto;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.model.User;
import com.example.FinalProject.model.UserLoyaltyProgram;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import com.example.FinalProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

public class UserLoyaltyProgramMapper {

    public static UserLoyaltyProgram toEntity(UserLoyaltyProgramDto userLoyaltyProgramDto, UserRepository userRepository, LoyaltyProgramRepository loyaltyProgramRepository) {
        User user = userRepository.findById(userLoyaltyProgramDto
            .getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository
            .findById(userLoyaltyProgramDto.getLoyaltyProgramId()).orElseThrow(() -> new EntityNotFoundException("Программа лояльности не найдена"));

        return UserLoyaltyProgram.builder()
                                 .user(user)
                                 .loyaltyProgram(loyaltyProgram)
                                 .build();
    }

    public static UserLoyaltyProgramDto toDto(UserLoyaltyProgram userLoyaltyProgram) {
        return UserLoyaltyProgramDto.builder()
                                    .userId(userLoyaltyProgram.getUser().getUserId())
                                    .loyaltyProgramId(userLoyaltyProgram.getLoyaltyProgram().getLoyaltyProgramId())
                                    .build();
    }
}
