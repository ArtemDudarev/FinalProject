package com.example.FinalProject.Service;

import com.example.FinalProject.model.User;
import com.example.FinalProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveUser(User user) {
        try {
            userRepository.save(user);
            log.info(String.format("Пользователь %s успешно сохранен", user.getEmail()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s пользователя", user.getEmail()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s пользователя", user.getEmail()), e);
        }
    }

    @Transactional
    public void updateUser(User user) {
        try {
            if (userRepository.existsById(user.getUserId())) {
                userRepository.save(user);
                log.info(String.format("Пользователь %s обновлен", user.getUserId()));
            } else {
                log.info("Пользователь не найден");
                throw new EntityNotFoundException("Пользователь не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s пользователя ", user.getEmail()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении пользователя %s", user.getEmail()), e);
        }
    }

    @Transactional(readOnly = true)
    public User findUserById(UUID userId) {
        try {
            log.info(String.format("Получение пользователя по ID: %s", userId));
            return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении пользователя по ID: %s", userId), e);
            throw new RuntimeException(String.format("Ошибка при получении пользователя по ID: %s", userId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        try {
            log.info("Получение всех пользователей");
            return userRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех пользователей", e);
            throw new RuntimeException("Ошибка при получении всех пользователей", e);
        }
    }

    @Transactional
    public void deleteUser(UUID userId) {
        try {
            userRepository.deleteById(userId);
            log.info(String.format("Пользователь с ID: %s успешно удален", userId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении пользователя с ID: %s", userId), e);
            throw new RuntimeException(String.format("Ошибка при удалении пользователя с ID: %s", userId), e);
        }
    }
}
