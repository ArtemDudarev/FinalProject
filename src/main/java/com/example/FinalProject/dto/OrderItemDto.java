package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDto {
    private Integer quantity;
    private Double pricePerItem;
    private UUID orderId;
    private UUID productId;
}