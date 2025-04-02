package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.dto.UserProfileDto;
import com.example.FinalProject.mapper.AddressMapper;
import com.example.FinalProject.mapper.UserMapper;
import com.example.FinalProject.model.Address;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.model.Role;
import com.example.FinalProject.model.User;
import com.example.FinalProject.model.UserLoyaltyProgram;
import com.example.FinalProject.repository.AddressRepository;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import com.example.FinalProject.repository.RoleRepository;
import com.example.FinalProject.repository.UserLoyaltyProgramRepository;
import com.example.FinalProject.repository.UserRepository;
import com.example.FinalProject.service.UserService;
import com.example.FinalProject.service.config.CustomOAuth2User;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImp.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AddressRepository addressRepository;
    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final UserLoyaltyProgramRepository userLoyaltyProgramRepository;

    public UserServiceImp(UserRepository userRepository, RoleRepository roleRepository,
                          AddressRepository addressRepository, LoyaltyProgramRepository loyaltyProgramRepository,
                          UserLoyaltyProgramRepository userLoyaltyProgramRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.userLoyaltyProgramRepository = userLoyaltyProgramRepository;
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        try {
            userDto.setRoleId(roleRepository.findByRoleName("ROLE_USER").get().getRoleId());
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
            User user = UserMapper.toEntity(userDto, roleRepository, addressRepository);

            userLoyaltyProgramRepository.save(UserLoyaltyProgram.builder()
                .user(user)
                .loyaltyProgram(loyaltyProgramRepository.findByProgramName("First user").get())
                .build());

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
                address.setAddressId((user.getAddress() != null ? user.getAddress().getAddressId() : null));
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
    public List<UserDto> findAllUsersWithRolesAndLoyaltyPrograms() {
        try {
            log.info("Получение всех пользователей с ролями и программами лояльности");
            List<User> users = userRepository.findAll();
            return users.stream().map(UserMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех пользователей с ролями и программами лояльности", e);
            throw new RuntimeException("Ошибка при получении всех пользователей с ролями и программами лояльности", e);
        }
    }

    @Transactional
    public void updateUserRole(String email, String roleName) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            Role role = roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("Роль не найдена"));
            user.setRole(role);
            userRepository.save(user);
            log.info(String.format("Роль пользователя %s обновлена", email));
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении роли пользователя %s", email), e);
            throw new RuntimeException(String.format("Ошибка при обновлении роли пользователя %s", email), e);
        }
    }

    @Transactional
    public void updateUserLoyaltyProgram(String email, String loyaltyProgramName) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findByProgramName(loyaltyProgramName).orElseThrow(() -> new EntityNotFoundException("Программа лояльности не найдена"));
//            UserLoyaltyProgram userLoyaltyProgram = UserLoyaltyProgram.builder()
//                .loyaltyProgram(loyaltyProgram)
//                .user(user)
//                .build();
            UserLoyaltyProgram userLoyaltyProgram = userLoyaltyProgramRepository.findByUserUserId(user.getUserId());
            userLoyaltyProgram.setLoyaltyProgram(loyaltyProgram);
//            userRepository.save(user);
            log.info(String.format("Программа лояльности пользователя %s обновлена", email));
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении программы лояльности пользователя %s", email), e);
            throw new RuntimeException(String.format("Ошибка при обновлении программы лояльности пользователя %s", email), e);
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
    public UUID findUserIdByUserEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден")).getUserId();
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

    @Override
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return new DefaultOAuth2UserService() {
            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
                OAuth2User oauth2User = super.loadUser(userRequest);
                String email = oauth2User.getAttribute("email");

                User user = userRepository.findByEmail(email).orElse(null);
                if (user == null) {
                    UserDto userDto = UserDto.builder()
                                             .email(email)
                                             .firstName(oauth2User.getAttribute("given_name"))
                                             .lastName("family_name")
                                             .password("password")
                                             .build();
                    Role role = roleRepository.findByRoleName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));
                    userDto.setRoleId(role.getRoleId());
                    user = UserMapper.toEntity(userDto, roleRepository, addressRepository);
                    userRepository.save(user);

                    userLoyaltyProgramRepository.save(UserLoyaltyProgram.builder()
                                                                        .user(user)
                                                                        .loyaltyProgram(loyaltyProgramRepository.findByProgramName("First user").get())
                                                                        .build());
                }

                Set<GrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));

                return new CustomOAuth2User(oauth2User, authorities);
            }
        };
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                             .orElseThrow(() -> new UsernameNotFoundException("Пользователь с именем " + username + " не найден"));
    }
}
