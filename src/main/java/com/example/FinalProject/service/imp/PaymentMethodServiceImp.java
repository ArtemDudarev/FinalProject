package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.mapper.PaymentMethodMapper;
import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.PaymentMethod;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.PaymentMethodRepository;
import com.example.FinalProject.service.PaymentMethodService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentMethodServiceImp implements PaymentMethodService {

    private static final Logger log = LoggerFactory.getLogger(PaymentMethodServiceImp.class);
    private final PaymentMethodRepository paymentMethodRepository;
    private final OrderRepository orderRepository;

    public PaymentMethodServiceImp(PaymentMethodRepository paymentMethodRepository, OrderRepository orderRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public PaymentMethodDto createPaymentMethod(PaymentMethodDto paymentMethodDto) {
        try {
            PaymentMethod paymentMethod = PaymentMethodMapper.toEntity(paymentMethodDto);
            paymentMethodRepository.save(paymentMethod);
            log.info(String.format("Способ оплаты %s успешно сохранен", paymentMethodDto.getMethodName()));
            return paymentMethodDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s способа оплаты", paymentMethodDto.getMethodName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s способа оплаты", paymentMethodDto.getMethodName()), e);
        }
    }

    @Transactional
    public void updatePaymentMethod(UUID paymentMethodId, PaymentMethodDto paymentMethodDto) {
        try {
            if (paymentMethodRepository.existsById(paymentMethodId)) {
                PaymentMethod paymentMethod = PaymentMethodMapper.toEntity(paymentMethodDto);
                paymentMethod.setPaymentMethodId(paymentMethodId);
                paymentMethodRepository.save(paymentMethod);
                log.info(String.format("Способ оплаты %s обновлен", paymentMethodDto.getMethodName()));
            } else {
                log.info("Способ оплаты не найден");
                throw new EntityNotFoundException("Способ оплаты не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s способа оплаты", paymentMethodDto.getMethodName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s способа оплаты", paymentMethodDto.getMethodName()), e);
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
    public PaymentMethodDto findPaymentMethodDtoById(UUID paymentMethodId) {
        try {
            log.info(String.format("Получение Dto способа оплаты по ID: %s", paymentMethodId));
            return PaymentMethodMapper.toDto(paymentMethodRepository.findById(paymentMethodId).orElseThrow(() -> new EntityNotFoundException("Способ оплаты не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto способа оплаты по ID: %s", paymentMethodId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto способа оплаты по ID: %s", paymentMethodId), e);
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

    @Transactional(readOnly = true)
    public List<PaymentMethodDto> findAllDto() {
        try {
            log.info("Получение всех Dto способов оплаты");
            List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
            return paymentMethods.stream().map(PaymentMethodMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto способов оплаты", e);
            throw new RuntimeException("Ошибка при получении всех Dto способов оплаты", e);
        }
    }

    @Transactional
    public void deletePaymentMethod(UUID paymentMethodId) {
        try {
            PaymentMethod deletedMethod = paymentMethodRepository.findByMethodName("Deleted method");
            List<Order> orders = orderRepository.findAllByPaymentMethodPaymentMethodId(paymentMethodId);
            for (Order order : orders){
                order.setPaymentMethod(deletedMethod);
            }
            paymentMethodRepository.deleteById(paymentMethodId);
            log.info(String.format("Способ оплаты с ID: %s успешно удален", paymentMethodId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении способа оплаты с ID: %s", paymentMethodId), e);
            throw new RuntimeException(String.format("Ошибка при удалении способа оплаты с ID: %s", paymentMethodId), e);
        }
    }
}
