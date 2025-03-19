package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private UUID productId;
    private String productName;
    private String description;
    private Double price;
    private Integer weightGrams;
    private Boolean isAvailable;
    private UUID categoryId;
}
