package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.model.Image;
import com.example.FinalProject.repository.ImageRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.imp.ImageServiceImp;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ImageServiceImp imageService;

    private ImageDto imageDto;
    private UUID productId;
    private UUID imageId;

    @BeforeEach
    void setUp() {
        imageDto = ImageDto.builder()
                           .imageUrl("test-image-url").build();
        productId = UUID.randomUUID();
        imageId = UUID.randomUUID();
    }

    @Test
    void testCreateImageException() {
        when(imageRepository.save(any(Image.class))).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.createImage(imageDto);
        });

        assertEquals("Ошибка при сохранении test-image-url изображения", exception.getMessage());
    }

    @Test
    void testCreateImageForProductException() {
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.createImageForProduct(productId, imageDto);
        });

        assertEquals("Ошибка при создании изображения test-image-url для продукта " + productId, exception.getMessage());
    }

    @Test
    void testUpdateImageException() {
        when(imageRepository.existsById(imageId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.updateImage(imageId, imageDto);
        });

        assertEquals("Ошибка при обновлении " + imageId + " изображения", exception.getMessage());
    }

    @Test
    void testFindImageDtoByIdException() {
        when(imageRepository.findById(imageId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.findImageDtoById(imageId);
        });

        assertEquals("Ошибка при получении Dto изображения по ID: " + imageId, exception.getMessage());
    }

    @Test
    void testFindAllByProductProductIdException() {
        when(imageRepository.findAllByProductProductId(productId)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imageService.findAllByProductProductId(productId);
        });

        assertEquals(String.format("Ошибка при получении изображения по ID продукта: %s", productId), exception.getMessage());
    }
}