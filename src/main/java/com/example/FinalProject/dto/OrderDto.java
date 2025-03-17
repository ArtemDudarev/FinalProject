package com.example.FinalProject.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDto {
    private LocalDateTime orderDate;
    private String orderStatus;
    private Double totalAmount;
    private UUID userId;
    private UUID paymentMethodId;
}