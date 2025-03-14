package com.example.FinalProject.service;

import com.example.FinalProject.model.Address;
import com.example.FinalProject.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressService {

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void createAddress(Address address) {
        try {
            addressRepository.save(address);
            log.info(String.format("Адрес %s успешно сохранен", address));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s адреса", address), e);
            throw new RuntimeException(String.format("Ошибка при %s сохранении адреса", address), e);
        }
    }

    @Transactional
    public void updateAddress(Address address) {
        try {
            if (addressRepository.existsById(address.getAddressId())) {
                addressRepository.save(address);
                log.info(String.format("Адрес %s обновлен", address.getAddressId()));
            } else {
                log.info("Адрес не найден");
                throw new EntityNotFoundException("Адрес не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s адреса", address.getAddressId()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s адреса", address.getAddressId()), e);
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
    public List<Address> findAll() {
        try {
            log.info("Получение всех адресов");
            return addressRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех адресов", e);
            throw new RuntimeException("Ошибка при получении всех адресов", e);
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
