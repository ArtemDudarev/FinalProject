package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private UUID categoryId;
    private String categoryName;
    private String description;
}