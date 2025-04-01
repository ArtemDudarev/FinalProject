package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.mapper.CategoryMapper;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.CategoryRepository;
import com.example.FinalProject.repository.ImageRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.imp.ProductServiceImp;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ProductServiceImp productService;

    private CategoryDto categoryDto;
    private UUID categoryId;
    private ProductDto productDto;
    private UUID productId;

    @BeforeEach
    void setUp(){
        categoryDto = CategoryDto.builder()
            .categoryName("Test cat")
            .description("Test description")
            .build();

        productDto = ProductDto.builder()
            .productName("Test prod")
            .price(123.0)
            .description("Test description")
            .isAvailable(true)
            .weightGrams(1234)
            .categoryId(categoryId)
            .build();

        productId = UUID.randomUUID();
    }

    @Test
    void testCreateProduct(){
        when(categoryRepository.findById(productDto.getCategoryId())).thenReturn(
            Optional.of(CategoryMapper.toEntity(categoryDto)));
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.createProduct(productDto));
        assertEquals(String.format("Ошибка при сохранении %s продукта", productDto.getProductName()), exception.getMessage());
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void testUpdateProductException() {
        when(productRepository.existsById(productId)).thenReturn(true);
        when(categoryRepository.findById(any())).thenReturn(Optional.of(CategoryMapper.toEntity(categoryDto)));
        when(productRepository.save(any())).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.updateProduct(productId, productDto));
        assertEquals(String.format("Ошибка при обновлении %s продукта", productDto.getProductName()), exception.getMessage());
        verify(productRepository, times(1)).existsById(productId);
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void testFindProductDtoByIdException() {
        when(productRepository.findById(productId)).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.findProductDtoById(productId));
        assertEquals(String.format("Ошибка при получении Dto продукта по ID: %s", productId), exception.getMessage());
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void testFindAllDtoException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.findAllDto());
        assertEquals("Ошибка при получении всех Dto продуктов", exception.getMessage());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindAllSortProductsException() {
        when(productRepository.findAll()).thenThrow(new RuntimeException());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productService.findAllSortProducts(null, null, null));
        assertEquals("Ошибка при получении всех продуктов", exception.getMessage());
        verify(productRepository, times(1)).findAll();
    }
}
