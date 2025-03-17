package com.example.FinalProject.service;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.mapper.ImageMapper;
import com.example.FinalProject.model.Image;
import com.example.FinalProject.repository.ImageRepository;
import com.example.FinalProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageService.class);
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public ImageService(ImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public ImageDto createImage(ImageDto imageDto) {
        try {
            Image image = ImageMapper.toEntity(imageDto, productRepository);
            imageRepository.save(image);
            log.info(String.format("Изображение %s успешно сохранено", image.getImageUrl()));
            return imageDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s изображения", imageDto.getImageUrl()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s изображения", imageDto.getImageUrl()), e);
        }
    }

    @Transactional
    public void updateImage(UUID imageId, ImageDto imageDto) {
        try {
            if (imageRepository.existsById(imageId)) {
                Image image = ImageMapper.toEntity(imageDto, productRepository);
                image.setImageId(imageId);
                imageRepository.save(image);
                log.info(String.format("Изображение %s обновлено", image.getImageId()));
            } else {
                log.info("Изображение не найдено");
                throw new EntityNotFoundException("Изображение не найдено");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s изображения", imageId), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s изображения", imageId), e);
        }
    }

    @Transactional(readOnly = true)
    public Image findImageById(UUID imageId) {
        try {
            log.info(String.format("Получение изображения по ID: %s", imageId));
            return imageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Изображение не найдено"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении изображения по ID: %s", imageId), e);
            throw new RuntimeException(String.format("Ошибка при получении изображения по ID: %s", imageId), e);
        }
    }

    @Transactional(readOnly = true)
    public ImageDto findImageDtoById(UUID imageId) {
        try {
            log.info(String.format("Получение Dto изображения по ID: %s", imageId));
            return ImageMapper.toDto(imageRepository.findById(imageId).orElseThrow(() -> new EntityNotFoundException("Изображение не найдено")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto изображения по ID: %s", imageId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto изображения по ID: %s", imageId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<ImageDto> findAllByProductProductId(UUID productId){
        try {
            log.info(String.format("Получение изображения по ID продукта: %s", productId));
            List<Image> images = imageRepository.findAllByProductProductId(productId);
            return images.stream().map(ImageMapper::toDto).toList();
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении изображения по ID продукта: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при получении изображения по ID продукта: %s", productId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Image> findAll() {
        try {
            log.info("Получение всех изображений");
            return imageRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех изображений", e);
            throw new RuntimeException("Ошибка при получении всех изображений", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ImageDto> findAllDto() {
        try {
            log.info("Получение всех Dto изображений");
            List<Image> images = imageRepository.findAll();
            return images.stream().map(ImageMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto изображений", e);
            throw new RuntimeException("Ошибка при получении всех Dto изображений", e);
        }
    }

    @Transactional
    public void deleteImage(UUID imageId) {
        try {
            log.info(String.format("Удаление изображения с ID: %s", imageId));
            imageRepository.deleteById(imageId);
            log.info(String.format("Изображение с ID %s успешно удалено", imageId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении изображения с ID: %s", imageId), e);
            throw new RuntimeException(String.format("Ошибка при удалении изображения с ID: %s", imageId), e);
        }
    }
}
