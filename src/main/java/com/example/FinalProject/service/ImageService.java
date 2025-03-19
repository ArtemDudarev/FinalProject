package com.example.FinalProject.service;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.model.Image;
import java.util.List;
import java.util.UUID;

public interface ImageService {

    ImageDto createImage(ImageDto imageDto);

    void updateImage(UUID imageId, ImageDto imageDto);

    Image findImageById(UUID imageId);

    ImageDto findImageDtoById(UUID imageId);

    List<ImageDto> findAllByProductProductId(UUID productId);

    List<Image> findAll();

    List<ImageDto> findAllDto();

    void deleteImage(UUID imageId);

    ImageDto createImageForProduct(UUID productId, ImageDto imageDto);
}
