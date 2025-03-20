package com.example.FinalProject.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.service.AddressService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    @Mock
    private AddressService addressService;

    @InjectMocks
    private AddressController addressController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private AddressDto addressDto;
    private UUID addressId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(addressController).build();
        objectMapper = new ObjectMapper();
        addressId = UUID.randomUUID();
        addressDto = AddressDto.builder()
                               .city("Test City")
                               .street("Test Street")
                               .houseNumber("123")
                               .apartmentNumber("45")
                               .build();
    }

    @Test
    void testCreateAddress() throws Exception {
        when(addressService.createAddress(any(AddressDto.class))).thenReturn(addressDto);

        mockMvc.perform(post("/api/addresses/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(addressDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.city").value("Test City"));

        verify(addressService, times(1)).createAddress(any(AddressDto.class));
    }

    @Test
    void testUpdateAddress() throws Exception {
        doNothing().when(addressService).updateAddress(eq(addressId), any(AddressDto.class));

        mockMvc.perform(put(    "/api/addresses/update/{id}", addressId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(addressDto)))
               .andExpect(status().isNoContent());

        verify(addressService, times(1)).updateAddress(eq(addressId), any(AddressDto.class));
    }

    @Test
    void testGetAddressById() throws Exception {
        when(addressService.findDtoAddressById(addressId)).thenReturn(addressDto);

        mockMvc.perform(get("/api/addresses/address/{id}", addressId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.city").value("Test City"));

        verify(addressService, times(1)).findDtoAddressById(addressId);
    }

    @Test
    void testGetAllAddresses() throws Exception {
        List<AddressDto> addressDtos = Arrays.asList(addressDto, AddressDto.builder().city("Another City").build());
        when(addressService.findAllDto()).thenReturn(addressDtos);

        mockMvc.perform(get("/api/addresses")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].city").value("Test City"))
               .andExpect(jsonPath("$[1].city").value("Another City"));

        verify(addressService, times(1)).findAllDto();
    }

    @Test
    void testDeleteAddress() throws Exception {
        doNothing().when(addressService).deleteAddress(addressId);

        mockMvc.perform(delete("/api/addresses/delete/{id}", addressId))
               .andExpect(status().isNoContent());

        verify(addressService, times(1)).deleteAddress(addressId);
    }
}
