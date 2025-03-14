package com.example.FinalProject.service;

import com.example.FinalProject.model.Image;
import com.example.FinalProject.repository.ImageRepository;
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

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public void createImage(Image image) {
        try {
            imageRepository.save(image);
            log.info(String.format("Изображение %s успешно сохранено", image.getImageId()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s изображения", image.getImageId()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s изображения", image.getImageId()), e);
        }
    }

    @Transactional
    public void updateImage(Image image) {
        try {
            if (imageRepository.existsById(image.getImageId())) {
                imageRepository.save(image);
                log.info(String.format("Изображение %s обновлено", image.getImageId()));
            } else {
                log.info("Изображение не найдено");
                throw new EntityNotFoundException("Изображение не найдено");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s изображения", image.getImageId()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s изображения", image.getImageId()), e);
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
    public List<Image> findAll() {
        try {
            log.info("Получение всех изображений");
            return imageRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех изображений", e);
            throw new RuntimeException("Ошибка при получении всех изображений", e);
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
