package com.example.FinalProject.service;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.model.Order;
import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(OrderDto orderDto);

    OrderDto createOrderBasket(UUID userId);

    void addOrderItem(UUID orderId, OrderItemDto orderItemDto);

    void confirmOrder(UUID orderId, PaymentMethodDto paymentMethodDto);

    Double getDiscount(Order order);

    void updateOrder(UUID orderId, OrderDto orderDto);

    Order findOrderById(UUID orderId);

    OrderDto findOrderDtoById(UUID orderId);

    List<Order> findAll();

    List<OrderDto> findAllDto();

    void deleteOrder(UUID orderId);
}
