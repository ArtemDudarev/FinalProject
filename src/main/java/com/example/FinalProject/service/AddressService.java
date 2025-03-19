package com.example.FinalProject.service;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.model.Address;
import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressDto createAddress(AddressDto addressDto);

    void updateAddress(UUID addressId, AddressDto addressDto);

    Address findAddressById(UUID addressId);

    AddressDto findDtoAddressById(UUID addressId);

    List<Address> findAll();

    List<AddressDto> findAllDto();

    void deleteAddress(UUID addressId);
}
