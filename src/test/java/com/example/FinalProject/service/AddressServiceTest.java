package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.model.Address;
import com.example.FinalProject.repository.AddressRepository;
import com.example.FinalProject.service.imp.AddressServiceImp;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImp addressService;

    @Test
    public void testCreateAddress() {
        AddressDto addressDto = AddressDto.builder()
                                          .city("City")
                                          .street("Street")
                                          .houseNumber("123")
                                          .apartmentNumber("45")
                                          .build();

        Address address = Address.builder()
                                 .city("City")
                                 .street("Street")
                                 .houseNumber("123")
                                 .apartmentNumber("45")
                                 .build();

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        AddressDto createdAddressDto = addressService.createAddress(addressDto);

        assertNotNull(createdAddressDto);
        assertEquals(addressDto.getCity(), createdAddressDto.getCity());
    }

    @Test
    public void testUpdateAddressException() {
        UUID addressId = UUID.randomUUID();
        AddressDto addressDto = AddressDto.builder()
                                          .city("Updated City")
                                          .street("Updated Street")
                                          .houseNumber("456")
                                          .apartmentNumber("67")
                                          .build();

        when(addressRepository.existsById(addressId)).thenThrow(RuntimeException.class);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> addressService.updateAddress(addressId, addressDto));
        assertEquals(String.format("Ошибка при обновлении %s адреса", addressId), exception.getMessage());
        verify(addressRepository, never()).save(any());
    }

    @Test
    public void testFindDtoAddressByIdException() {
        UUID addressId = UUID.randomUUID();
        when(addressRepository.findById(addressId)).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.findDtoAddressById(addressId));
        assertEquals(String.format("Ошибка при получении Dto адреса по ID: %s", addressId), exception.getMessage());
        verify(addressRepository, times(1)).findById(addressId);
    }

    @Test
    public void testFindAllAddressesException() {
        when(addressRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.findAll());
        assertEquals("Ошибка при получении всех адресов", exception.getMessage());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllDtoException() {
        when(addressRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.findAllDto());
        assertEquals("Ошибка при получении всех Dto адресов", exception.getMessage());
        verify(addressRepository, times(1)).findAll();
    }

    @Test
    public void testDeleteAddressException() {
        UUID addressId = UUID.randomUUID();
        doThrow(new RuntimeException("Simulated exception")).when(addressRepository).deleteById(addressId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> addressService.deleteAddress(addressId));
        assertEquals(String.format("Ошибка при удалении адреса с ID: %s", addressId), exception.getMessage());
        verify(addressRepository, times(1)).deleteById(addressId);
    }
}
