package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.model.Image;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

public class ImageMapper {

    public static Image toEntity(ImageDto productImageDTO, ProductRepository productRepository) {
        Product product = productRepository.findById(productImageDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

        return Image.builder()
                           .imageUrl(productImageDTO.getImageUrl())
                           .product(product)
                           .build();
    }

    public static ImageDto toDto(Image productImage) {
        return ImageDto.builder()
                              .imageUrl(productImage.getImageUrl())
                              .productId(productImage.getProduct().getProductId())
                              .build();
    }
}
