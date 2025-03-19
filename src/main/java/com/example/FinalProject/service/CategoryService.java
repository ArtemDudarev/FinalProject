package com.example.FinalProject.service;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.model.Category;
import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    void updateCategory(UUID categoryId, CategoryDto categoryDto);

    Category findCategoryById(UUID categoryId);

    CategoryDto findDtoCategoryById(UUID categoryId);

    List<Category> findAll();

    List<CategoryDto> findAllDto();

    void deleteCategory(UUID categoryId);
}
