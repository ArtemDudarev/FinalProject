package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.OrderItem;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

public class OrderItemMapper {
    public static OrderItem toEntity(OrderItemDto orderItemDto, OrderRepository orderRepository, ProductRepository productRepository) {
        Order order = orderRepository.findById(orderItemDto
            .getOrderId()).orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
        Product product = productRepository.findById(orderItemDto
            .getProductId()).orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        return OrderItem.builder()
                        .quantity(orderItemDto.getQuantity())
                        .pricePerItem(orderItemDto.getPricePerItem())
                        .order(order)
                        .product(product)
                        .build();
    }

    public static OrderItemDto toDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                           .quantity(orderItem.getQuantity())
                           .pricePerItem(orderItem.getPricePerItem())
                           .orderId(orderItem.getOrder().getOrderId())
                           .productId(orderItem.getProduct().getProductId())
                           .build();
    }
}
