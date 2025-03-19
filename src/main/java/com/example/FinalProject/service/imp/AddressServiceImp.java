package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.mapper.AddressMapper;
import com.example.FinalProject.model.Address;
import com.example.FinalProject.repository.AddressRepository;
import com.example.FinalProject.service.AddressService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressServiceImp implements AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressServiceImp.class);
    private final AddressRepository addressRepository;

    public AddressServiceImp(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public AddressDto createAddress(AddressDto addressDto) {
        try {
            Address address = AddressMapper.toEntity(addressDto);
            addressRepository.save(address);
            log.info(String.format("Адрес %s успешно сохранен", address));
            return addressDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s адреса", addressDto), e);
            throw new RuntimeException(String.format("Ошибка при %s сохранении адреса", addressDto), e);
        }
    }

    @Transactional
    public void updateAddress(UUID addressId, AddressDto addressDto) {
        try {
            if (addressRepository.existsById(addressId)) {
                Address address = AddressMapper.toEntity(addressDto);
                address.setAddressId(addressId);
                addressRepository.save(address);
                log.info(String.format("Адрес %s обновлен", address.getAddressId()));
            } else {
                log.info("Адрес не найден");
                throw new EntityNotFoundException("Адрес не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s адреса", addressId), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s адреса", addressId), e);
        }
    }

    @Transactional(readOnly = true)
    public Address findAddressById(UUID addressId) {
        try {
            log.info(String.format("Получение адреса по ID: %s", addressId));
            return addressRepository.findById(addressId).orElseThrow(() -> new EntityNotFoundException("Адрес не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении адреса по ID: %s", addressId), e);
            throw new RuntimeException(String.format("Ошибка при получении адреса по ID: %s", addressId), e);
        }
    }

    @Transactional(readOnly = true)
    public AddressDto findDtoAddressById(UUID addressId) {
        try {
            log.info(String.format("Получение Dto адреса по ID: %s", addressId));
            return AddressMapper.toDto(addressRepository.findById(addressId).orElseThrow(() -> new EntityNotFoundException("Адрес не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto адреса по ID: %s", addressId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto адреса по ID: %s", addressId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Address> findAll() {
        try {
            log.info("Получение всех адресов");
            return addressRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех адресов", e);
            throw new RuntimeException("Ошибка при получении всех адресов", e);
        }
    }

    @Transactional(readOnly = true)
    public List<AddressDto> findAllDto() {
        try {
            log.info("Получение всех Dto адресов");
            List<Address> addresses = addressRepository.findAll();
            return addresses.stream().map(AddressMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto адресов", e);
            throw new RuntimeException("Ошибка при получении всех Dto адресов", e);
        }
    }

    @Transactional
    public void deleteAddress(UUID addressId) {
        try {
            addressRepository.deleteById(addressId);
            log.info(String.format("Адрес с ID: %s успешно удален", addressId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении адреса с ID: %s", addressId), e);
            throw new RuntimeException(String.format("Ошибка при удалении адреса с ID: %s", addressId), e);
        }
    }
}
