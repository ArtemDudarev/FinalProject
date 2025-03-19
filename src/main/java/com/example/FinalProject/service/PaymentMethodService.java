package com.example.FinalProject.service;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.model.PaymentMethod;
import java.util.List;
import java.util.UUID;

public interface PaymentMethodService {

    PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto);

    void updatePaymentMethod(UUID paymentMethodId, PaymentMethodDto paymentMethodDto);

    PaymentMethod findPaymentMethodById(UUID paymentMethodId);

    PaymentMethodDto findPaymentMethodDtoById(UUID paymentMethodId);

    List<PaymentMethod> findAll();

    List<PaymentMethodDto> findAllDto();

    void deletePaymentMethod(UUID paymentMethodId);
}
