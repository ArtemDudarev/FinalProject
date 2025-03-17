package com.example.FinalProject.service;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.dto.UserProfileDto;
import com.example.FinalProject.mapper.AddressMapper;
import com.example.FinalProject.mapper.UserMapper;
import com.example.FinalProject.model.Address;
import com.example.FinalProject.model.User;
import com.example.FinalProject.repository.AddressRepository;
import com.example.FinalProject.repository.RoleRepository;
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
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       AddressRepository addressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        try {
            userDto.setRoleId(roleRepository.findByRoleName("ROLE_USER").get().getRoleId());
            User user = UserMapper.toEntity(userDto, roleRepository, addressRepository);
            userRepository.save(user);
            log.info(String.format("Пользователь %s успешно сохранен", userDto.getEmail()));
            return userDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s пользователя", userDto.getEmail()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s пользователя", userDto.getEmail()), e);
        }
    }

    @Transactional
    public void updateUser(UUID userId, UserDto userDto) {
        try {
            if (userRepository.existsById(userId)) {
                User user = UserMapper.toEntity(userDto, roleRepository, addressRepository);
                user.setUserId(userId);
                userRepository.save(user);
                log.info(String.format("Пользователь %s обновлен", user.getUserId()));
            } else {
                log.info("Пользователь не найден");
                throw new EntityNotFoundException("Пользователь не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s пользователя ", userDto.getEmail()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении пользователя %s", userDto.getEmail()), e);
        }
    }

    @Transactional
    public void updateProfileUser(UUID userId, UserProfileDto userProfileDto) {
        try {
            if (userRepository.existsById(userId)) {
                User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
                user.setUserId(userId);
                Address address = AddressMapper.toEntity(userProfileDto.getAddressDto());
                address.setAddressId(user.getAddress().getAddressId());
                user.setAddress(address);
                user.setFirstName(userProfileDto.getFirstName());
                user.setLastName(userProfileDto.getLastName());
                user.setEmail(userProfileDto.getEmail());
                user.setPhoneNumber(userProfileDto.getPhoneNumber());
                userRepository.save(user);
                log.info(String.format("Пользователь %s обновлен", user.getUserId()));
            } else {
                log.info("Пользователь не найден");
                throw new EntityNotFoundException("Пользователь не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s пользователя ", userProfileDto.getEmail()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении пользователя %s", userProfileDto.getEmail()), e);
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
    public UserDto findUseDtoById(UUID userId) {
        try {
            log.info(String.format("Получение Dto пользователя по ID: %s", userId));
            return UserMapper.toDto(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto пользователя по ID: %s", userId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto пользователя по ID: %s", userId), e);
        }
    }

    @Transactional(readOnly = true)
    public UserProfileDto findUseProfileDtoById(UUID userId) {
        try {
            log.info(String.format("Получение Dto пользователя по ID: %s", userId));
            return UserMapper.toProfileDto(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto пользователя по ID: %s", userId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto пользователя по ID: %s", userId), e);
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

    @Transactional(readOnly = true)
    public List<UserDto> findAllDto() {
        try {
            log.info("Получение всех Dto пользователей");
            List<User> users = userRepository.findAll();
            return users.stream().map(UserMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto пользователей", e);
            throw new RuntimeException("Ошибка при получении всех Dto пользователей", e);
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
