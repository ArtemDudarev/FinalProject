package com.example.FinalProject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodDto {
    private String methodName;
}
