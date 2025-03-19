package com.example.FinalProject.service;

import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.model.OrderItem;
import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    OrderItemDto createOrderItem(OrderItemDto orderItemDto);

    void updateOrderItem(UUID orderItemId, OrderItemDto orderItemDto);

    OrderItem findOrderItemById(UUID orderItemId);

    OrderItemDto findOrderItemDtoById(UUID orderItemId);

    List<OrderItem> findAll();

    List<OrderItemDto> findAllDto();

    void deleteOrderItem(UUID orderItemId);
}
