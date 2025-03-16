package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.model.Address;

public class AddressMapper {

    public static Address toEntity(AddressDto addressDto) {
        return Address.builder()
                      .city(addressDto.getCity())
                      .street(addressDto.getStreet())
                      .houseNumber(addressDto.getHouseNumber())
                      .apartmentNumber(addressDto.getApartmentNumber())
                      .build();
    }

    public static AddressDto toDto(Address address) {
        return AddressDto.builder()
                         .city(address.getCity())
                         .street(address.getStreet())
                         .houseNumber(address.getHouseNumber())
                         .apartmentNumber(address.getApartmentNumber())
                         .build();
    }
}
