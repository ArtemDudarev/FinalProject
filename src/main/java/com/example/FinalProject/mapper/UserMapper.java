package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.UserDto;
import com.example.FinalProject.dto.UserProfileDto;
import com.example.FinalProject.model.Address;
import com.example.FinalProject.model.Role;
import com.example.FinalProject.model.User;
import com.example.FinalProject.repository.AddressRepository;
import com.example.FinalProject.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;

public class UserMapper {

    public static User toEntity(UserDto userDto, RoleRepository roleRepository, AddressRepository addressRepository) {
        Role role = roleRepository.findById(userDto.getRoleId()).orElseThrow(() -> new EntityNotFoundException("Роль не найдена"));
        Address address;
        if (userDto.getAddressId() != null) {
            address = addressRepository.findById(userDto.getAddressId()).orElseThrow(
                () -> new EntityNotFoundException("Адрес не найден"));
        } else {
            address = null;
        }

        return User.builder()
                   .firstName(userDto.getFirstName())
                   .lastName(userDto.getLastName())
                   .email(userDto.getEmail())
                   .password(userDto.getPassword())
                   .phoneNumber(userDto.getPhoneNumber())
                   .role(role)
                   .address(address)
                   .build();
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .email(user.getEmail())
                      .password(user.getPassword())
                      .phoneNumber(user.getPhoneNumber())
                      .roleId(user.getRole().getRoleId())
                      .addressId(user.getAddress() != null ? user.getAddress().getAddressId() : null)
                      .build();
    }

    public static UserProfileDto toProfileDto(User user) {
        return UserProfileDto.builder()
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .email(user.getEmail())
                      .phoneNumber(user.getPhoneNumber())
                      .addressDto(AddressMapper.toDto(user.getAddress() != null ? user.getAddress() : new Address()))
                      .build();
    }
}
