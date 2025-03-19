package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.model.PaymentMethod;

public class PaymentMethodMapper {

    public static PaymentMethod toEntity(PaymentMethodDto paymentMethodDto) {
        return PaymentMethod.builder()
                            .methodName(paymentMethodDto.getMethodName())
                            .build();
    }

    public static PaymentMethodDto toDto(PaymentMethod paymentMethod) {
        return PaymentMethodDto.builder()
            .paymentMethodId(paymentMethod.getPaymentMethodId())
            .methodName(paymentMethod.getMethodName())
            .build();
    }
}