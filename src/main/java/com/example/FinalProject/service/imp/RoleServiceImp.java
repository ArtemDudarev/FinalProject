package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.RoleDto;
import com.example.FinalProject.mapper.RoleMapper;
import com.example.FinalProject.model.Role;
import com.example.FinalProject.repository.RoleRepository;
import com.example.FinalProject.repository.UserRepository;
import com.example.FinalProject.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImp implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImp.class);
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleServiceImp(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RoleDto createRole(RoleDto roleDto) {
        try {
            Role role = RoleMapper.toEntity(roleDto);
            roleRepository.save(role);
            log.info(String.format("Роль %s успешно сохранена", role.getRoleName()));
            return RoleMapper.toDto(role);
        } catch (Exception e) {
            log.error(String.format("Ошибка при схранении %s роли", roleDto.getRoleName()));
            throw new RuntimeException(String.format("Ошибка при схранении %s роли", roleDto.getRoleName()), e);
        }
    }

    @Transactional
    public void updateRole(UUID roleId, RoleDto roleDto) {
        try {
            if (roleRepository.existsById(roleId)) {
                Role role = RoleMapper.toEntity(roleDto);
                role.setRoleId(roleId);
                roleRepository.save(role);
                log.info(String.format("Роль %s обновлена", role.getRoleName()));
            } else {
                log.info("Роль не найдена");
                throw new EntityNotFoundException("Роль не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s роли", roleDto.getRoleName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s роли", roleDto.getRoleName()), e);
        }
    }

    @Transactional(readOnly = true)
    public Role findRoleById(UUID roleId) {
        try {
            log.info(String.format("Получение роли по ID: %s", roleId));
            return roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Роль не найдена"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении роли по ID: %s", roleId.toString()), e);
            throw new RuntimeException(String.format("Ошибка при получении роли по ID: %s", roleId), e);
        }
    }

    @Transactional(readOnly = true)
    public RoleDto findRoleDtoById(UUID roleId) {
        try {
            log.info(String.format("Получение роли по ID: %s", roleId));
            return RoleMapper.toDto(roleRepository.findById(roleId).orElseThrow(() -> new EntityNotFoundException("Роль не найдена")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении роли по ID: %s", roleId), e);
            throw new RuntimeException(String.format("Ошибка при получении роли по ID: %s", roleId), e);
        }
    }

    @Transactional(readOnly = true)
    public Role findRoleByName(String roleName) {
        try {
            log.info(String.format("Получение роли по имени: %s", roleName));
            return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new EntityNotFoundException("Роль не найдена"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении роли по имени: %s", roleName), e);
            throw new RuntimeException(String.format("Ошибка при получении роли по имени: %s", roleName), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Role> findAll() {
        try {
            log.info("Получение всех ролей");
            return roleRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех ролей", e);
            throw new RuntimeException("Ошибка при получении всех ролей", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RoleDto> findAllDto() {
        try {
            log.info("Получение всех Dto ролей");
            List<Role> roles = roleRepository.findAll();
            return roles.stream().map(RoleMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto ролей", e);
            throw new RuntimeException("Ошибка при получении всех Dto ролей", e);
        }
    }

    @Transactional
    public void deleteRole(UUID roleId) {
        try {
            Role role = roleRepository.findById(roleId).orElseThrow(
                () -> new EntityNotFoundException("Роль не найдена"));

            Role defaultRole = roleRepository.findByRoleName("ROLE_USER").orElseThrow(
                () -> new EntityNotFoundException("Роль ROLE_USER не найдена"));

            userRepository.findAllByRole(role).forEach(user -> {
                user.setRole(defaultRole);
                userRepository.save(user);
            });

            roleRepository.deleteById(roleId);
            log.info(String.format("Роль с ID %s успешно удалена", roleId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении роли с ID: %s", roleId), e);
            throw new RuntimeException(String.format("Ошибка при удалении роли с ID: %s", roleId), e);
        }

    }
}
