package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.model.Category;

public class CategoryMapper {

    public static Category toEntity(CategoryDto categoryDto) {
        return Category.builder()
                       .categoryName(categoryDto.getCategoryName())
                       .description(categoryDto.getDescription())
                       .build();
    }

    public static CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                          .categoryName(category.getCategoryName())
                          .description(category.getDescription())
                          .build();
    }
}
