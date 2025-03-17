package com.example.FinalProject.controller;

import com.example.FinalProject.dto.AddressDto;
import com.example.FinalProject.service.AddressService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/create")
    public ResponseEntity<AddressDto> createAddress(@RequestBody AddressDto addressDto) {
        AddressDto createdAddress = addressService.createAddress(addressDto);
        return ResponseEntity.ok(createdAddress);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateAddress(@PathVariable UUID id, @RequestBody AddressDto addressDto) {
        addressService.updateAddress(id, addressDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable UUID id) {
        AddressDto addressDto = addressService.findDtoAddressById(id);
        return ResponseEntity.ok(addressDto);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        List<AddressDto> addresses = addressService.findAllDto();
        return ResponseEntity.ok(addresses);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}
