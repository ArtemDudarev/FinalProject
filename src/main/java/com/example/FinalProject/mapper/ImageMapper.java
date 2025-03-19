package com.example.FinalProject.mapper;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.model.Image;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;

public class ImageMapper {

    public static Image toEntity(ImageDto imageDTO, ProductRepository productRepository) {

        Product product = null;

        if(imageDTO.getProductId() != null){
            product = productRepository
                .findById(imageDTO.getProductId()).orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));
        }

        return Image.builder()
                           .imageUrl(imageDTO.getImageUrl())
                           .product(product)
                           .build();
    }

    public static ImageDto toDto(Image image) {
        return ImageDto.builder()
                              .imageUrl(image.getImageUrl())
                              .productId(image.getProduct().getProductId())
                              .build();
    }
}
