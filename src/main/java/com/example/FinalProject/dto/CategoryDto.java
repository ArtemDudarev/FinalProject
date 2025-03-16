package com.example.FinalProject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDto {
    private String categoryName;
    private String description;
}