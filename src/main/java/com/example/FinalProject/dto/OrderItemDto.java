package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Integer quantity;
    private Double pricePerItem;
    private UUID orderId;
    private UUID productId;
}