package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.model.Category;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;

public class ProductMapper {

    public static Product toEntity(ProductDto productDto, CategoryRepository categoryRepository) {
        Category category = categoryRepository.findById(productDto
            .getCategoryId()).orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        return Product.builder()
                      .productName(productDto.getProductName())
                      .description(productDto.getDescription())
                      .price(productDto.getPrice())
                      .weightGrams(productDto.getWeightGrams())
                      .isAvailable(productDto.getIsAvailable())
                      .category(category)
                      .build();
    }

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                         .productId(product.getProductId())
                         .productName(product.getProductName())
                         .description(product.getDescription())
                         .price(product.getPrice())
                         .weightGrams(product.getWeightGrams())
                         .isAvailable(product.getIsAvailable())
                         .categoryId(product.getCategory().getCategoryId())
                         .build();
    }
}
