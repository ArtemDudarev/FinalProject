package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentMethodDto {
    private UUID paymentMethodId;
    private String methodName;
}
