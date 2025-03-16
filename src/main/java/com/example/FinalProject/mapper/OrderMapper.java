package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.PaymentMethod;
import com.example.FinalProject.model.User;
import com.example.FinalProject.repository.PaymentMethodRepository;
import com.example.FinalProject.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

public class OrderMapper {
    public static Order toEntity(OrderDto orderDto, UserRepository userRepository, PaymentMethodRepository paymentMethodRepository) {
        User user = userRepository.findById(orderDto
            .getUserId()).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        PaymentMethod paymentMethod = paymentMethodRepository.findById(orderDto
            .getPaymentMethodId()).orElseThrow(() -> new EntityNotFoundException("Способ оплаты не найден"));

        return Order.builder()
                    .orderDate(orderDto.getOrderDate())
                    .orderStatus(orderDto.getOrderStatus())
                    .totalAmount(orderDto.getTotalAmount())
                    .user(user)
                    .paymentMethod(paymentMethod)
                    .build();
    }

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                       .orderDate(order.getOrderDate())
                       .orderStatus(order.getOrderStatus())
                       .totalAmount(order.getTotalAmount())
                       .userId(order.getUser().getUserId())
                       .paymentMethodId(order.getPaymentMethod().getPaymentMethodId())
                       .build();
    }
}
