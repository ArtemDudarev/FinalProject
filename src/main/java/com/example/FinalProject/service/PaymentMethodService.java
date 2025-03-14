package com.example.FinalProject.service;

import com.example.FinalProject.model.PaymentMethod;
import com.example.FinalProject.repository.PaymentMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentMethodService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodService.class);
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Transactional
    public void createPaymentMethod(PaymentMethod paymentMethod) {
        try {
            paymentMethodRepository.save(paymentMethod);
            log.info(String.format("Способ оплаты %s успешно сохранен", paymentMethod.getMethodName()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s способа оплаты", paymentMethod.getMethodName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s способа оплаты", paymentMethod.getMethodName()), e);
        }
    }

    @Transactional
    public void updatePaymentMethod(PaymentMethod paymentMethod) {
        try {
            if (paymentMethodRepository.existsById(paymentMethod.getPaymentMethodId())) {
                paymentMethodRepository.save(paymentMethod);
                log.info(String.format("Способ оплаты %s обновлен", paymentMethod.getMethodName()));
            } else {
                log.info("Способ оплаты не найден");
                throw new EntityNotFoundException("Способ оплаты не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s способа оплаты", paymentMethod.getMethodName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s способа оплаты", paymentMethod.getMethodName()), e);
        }
    }

    @Transactional(readOnly = true)
    public PaymentMethod findPaymentMethodById(UUID paymentMethodId) {
        try {
            log.info(String.format("Получение способа оплаты по ID: %s", paymentMethodId));
            return paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new EntityNotFoundException("Способ оплаты не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении способа оплаты по ID: %s", paymentMethodId), e);
            throw new RuntimeException(String.format("Ошибка при получении способа оплаты по ID: %s", paymentMethodId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<PaymentMethod> findAll() {
        try {
            log.info("Получение всех способов оплаты");
            return paymentMethodRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех способов оплаты", e);
            throw new RuntimeException("Ошибка при получении всех способов оплаты", e);
        }
    }

    @Transactional
    public void deletePaymentMethod(UUID paymentMethodId) {
        try {
            paymentMethodRepository.deleteById(paymentMethodId);
            log.info(String.format("Способ оплаты с ID: %s успешно удален", paymentMethodId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении способа оплаты с ID: %s", paymentMethodId), e);
            throw new RuntimeException(String.format("Ошибка при удалении способа оплаты с ID: %s", paymentMethodId), e);
        }
    }
}
