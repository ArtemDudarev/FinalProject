package com.example.FinalProject.service;

import com.example.FinalProject.model.UserLoyaltyProgram;
import com.example.FinalProject.repository.UserLoyaltyProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLoyaltyProgramService {

    private static final Logger log = LoggerFactory.getLogger(UserLoyaltyProgramService.class);
    private final UserLoyaltyProgramRepository userLoyaltyProgramRepository;

    public UserLoyaltyProgramService(UserLoyaltyProgramRepository userLoyaltyProgramRepository) {
        this.userLoyaltyProgramRepository = userLoyaltyProgramRepository;
    }

    @Transactional
    public void createUserLoyaltyProgram(UserLoyaltyProgram userLoyaltyProgram) {
        try {
            userLoyaltyProgramRepository.save(userLoyaltyProgram);
            log.info(String.format("UserLoyaltyProgram %s успешно сохранена", userLoyaltyProgram.getUserLoyaltyProgramId()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении UserLoyaltyProgram %s", userLoyaltyProgram.getUserLoyaltyProgramId()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении UserLoyaltyProgram %s", userLoyaltyProgram.getUserLoyaltyProgramId()), e);
        }
    }

    @Transactional
    public void updateUserLoyaltyProgram(UserLoyaltyProgram userLoyaltyProgram) {
        try {
            if (userLoyaltyProgramRepository.existsById(userLoyaltyProgram.getUserLoyaltyProgramId())) {
                userLoyaltyProgramRepository.save(userLoyaltyProgram);
                log.info(String.format("UserLoyaltyProgram %s обновлена", userLoyaltyProgram.getUserLoyaltyProgramId()));
            } else {
                log.info("UserLoyaltyProgram не найдена");
                throw new EntityNotFoundException("UserLoyaltyProgram не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении UserLoyaltyProgram %s", userLoyaltyProgram.getUserLoyaltyProgramId()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении UserLoyaltyProgram %s", userLoyaltyProgram.getUserLoyaltyProgramId()), e);
        }
    }

    @Transactional(readOnly = true)
    public UserLoyaltyProgram findUserLoyaltyProgramById(UUID userLoyaltyProgramId) {
        try {
            log.info(String.format("Получение UserLoyaltyProgram по ID: %s", userLoyaltyProgramId));
            return userLoyaltyProgramRepository.findById(userLoyaltyProgramId).orElseThrow(() -> new EntityNotFoundException("Программа лояльности пользователя не найдена"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении UserLoyaltyProgram по ID: %s", userLoyaltyProgramId), e);
            throw new RuntimeException(String.format("Ошибка при получении UserLoyaltyProgram по ID: %s", userLoyaltyProgramId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<UserLoyaltyProgram> findAll() {
        try {
            log.info("Получение всех UserLoyaltyProgram");
            return userLoyaltyProgramRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех UserLoyaltyProgram", e);
            throw new RuntimeException("Ошибка при получении всех UserLoyaltyProgram", e);
        }
    }

    @Transactional
    public void deleteUserLoyaltyProgram(UUID userLoyaltyProgramId) {
        try {
            userLoyaltyProgramRepository.deleteById(userLoyaltyProgramId);
            log.info(String.format("UserLoyaltyProgram с ID: %s успешно удалена", userLoyaltyProgramId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении UserLoyaltyProgram с ID: %s", userLoyaltyProgramId), e);
            throw new RuntimeException(String.format("Ошибка при удалении UserLoyaltyProgram с ID: %s", userLoyaltyProgramId), e);
        }
    }
}
