package com.example.FinalProject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {
    private String city;
    private String street;
    private String houseNumber;
    private String apartmentNumber;
}
