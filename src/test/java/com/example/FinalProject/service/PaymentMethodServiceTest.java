package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.mapper.PaymentMethodMapper;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.PaymentMethodRepository;
import com.example.FinalProject.service.imp.PaymentMethodServiceImp;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentMethodServiceTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private PaymentMethodServiceImp paymentMethodService;

    private PaymentMethodDto paymentMethodDto;
    private UUID paymentMethodId;

    @BeforeEach
    void setUp() {
        paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setMethodName("Test Payment Method");
        paymentMethodId = UUID.randomUUID();
    }

    @Test
    void testCreatePaymentMethodException() {
        when(paymentMethodRepository.save(PaymentMethodMapper.toEntity(paymentMethodDto))).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.createPaymentMethod(paymentMethodDto));
        assertEquals("Ошибка при сохранении Test Payment Method способа оплаты", exception.getMessage());
    }

    @Test
    void testUpdatePaymentMethodException() {
        when(paymentMethodRepository.existsById(paymentMethodId)).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.updatePaymentMethod(paymentMethodId, paymentMethodDto));
        assertEquals("Ошибка при обновлении Test Payment Method способа оплаты", exception.getMessage());
    }

    @Test
    void testFindPaymentMethodByIdException() {
        when(paymentMethodRepository.findById(paymentMethodId)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.findPaymentMethodById(paymentMethodId));
        assertEquals("Ошибка при получении способа оплаты по ID: " + paymentMethodId, exception.getMessage());
    }


    @Test
    void testFindPaymentMethodDtoByIdException() {
        when(paymentMethodRepository.findById(paymentMethodId)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.findPaymentMethodDtoById(paymentMethodId));
        assertEquals("Ошибка при получении Dto способа оплаты по ID: " + paymentMethodId, exception.getMessage());
    }

    @Test
    void testFindAllException() {
        when(paymentMethodRepository.findAll()).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.findAll());
        assertEquals("Ошибка при получении всех способов оплаты", exception.getMessage());
    }

    @Test
    void testFindAllDtoException() {
        when(paymentMethodRepository.findAll()).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.findAllDto());
        assertEquals("Ошибка при получении всех Dto способов оплаты", exception.getMessage());
    }

    @Test
    void testDeletePaymentMethodException() {
        doThrow(new RuntimeException("Test Exception")).when(paymentMethodRepository).deleteById(paymentMethodId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> paymentMethodService.deletePaymentMethod(paymentMethodId));
        assertEquals("Ошибка при удалении способа оплаты с ID: " + paymentMethodId, exception.getMessage());
    }
}
