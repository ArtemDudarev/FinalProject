package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.mapper.CategoryMapper;
import com.example.FinalProject.model.Category;
import com.example.FinalProject.repository.CategoryRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.imp.CategoryServiceImp;
import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CategoryServiceImp categoryService;

    private UUID categoryId;
    private CategoryDto categoryDto;
    private Category category;

    @BeforeEach
    void setUp() {
        categoryId = UUID.randomUUID();
        categoryDto = CategoryDto.builder().categoryName("Test Category").build();
        category = CategoryMapper.toEntity(categoryDto);
        category.setCategoryId(categoryId);
    }

    @Test
    public void testCreateCategorySuccess() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto);
        assertNotNull(createdCategoryDto);
        assertEquals(categoryDto.getCategoryName(), createdCategoryDto.getCategoryName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testCreateCategoryException() {
        when(categoryRepository.save(any(Category.class))).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.createCategory(categoryDto));
        assertEquals(String.format("Ошибка при сохранении %s категории", categoryDto.getCategoryName()), exception.getMessage());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategorySuccess() {
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        categoryService.updateCategory(categoryId, categoryDto);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testUpdateCategoryNotFound() {
        when(categoryRepository.existsById(categoryId)).thenReturn(false);
        RuntimeException
            exception = assertThrows(RuntimeException.class, () -> categoryService.updateCategory(categoryId, categoryDto));
        assertEquals("Ошибка при обновлении Test Category категории", exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testUpdateCategoryException() {
        when(categoryRepository.existsById(categoryId)).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.updateCategory(categoryId, categoryDto));
        assertEquals(String.format("Ошибка при обновлении %s категории", categoryDto.getCategoryName()), exception.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    public void testFindCategoryByIdSuccess() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Category foundCategory = categoryService.findCategoryById(categoryId);
        assertNotNull(foundCategory);
        assertEquals(category.getCategoryName(), foundCategory.getCategoryName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindCategoryByIdNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findCategoryById(categoryId));
        assertEquals(String.format("Ошибка при получении категории по ID: %s", categoryId), exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindCategoryByIdException() {
        when(categoryRepository.findById(categoryId)).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findCategoryById(categoryId));
        assertEquals(String.format("Ошибка при получении категории по ID: %s", categoryId), exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindDtoCategoryByIdSuccess() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        CategoryDto foundCategoryDto = categoryService.findDtoCategoryById(categoryId);
        assertNotNull(foundCategoryDto);
        assertEquals(categoryDto.getCategoryName(), foundCategoryDto.getCategoryName());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindDtoCategoryByIdNotFound() {
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findDtoCategoryById(categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindDtoCategoryByIdException() {
        when(categoryRepository.findById(categoryId)).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findDtoCategoryById(categoryId));
        assertEquals(String.format("Ошибка при получении категории по ID: %s", categoryId), exception.getMessage());
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    public void testFindAllSuccess() {
        List<Category> categories = Arrays.asList(category, Category.builder().categoryName("Another Category").build());
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> foundCategories = categoryService.findAll();
        assertNotNull(foundCategories);
        assertEquals(categories.size(), foundCategories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllException() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findAll());
        assertEquals("Ошибка при получении всех категорий", exception.getMessage());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllDtoSuccess() {
        List<Category> categories = Arrays.asList(category, Category.builder().categoryName("Another Category").build());
        when(categoryRepository.findAll()).thenReturn(categories);
        List<CategoryDto> foundCategoryDtos = categoryService.findAllDto();
        assertNotNull(foundCategoryDtos);
        assertEquals(categories.size(), foundCategoryDtos.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindAllDtoException() {
        when(categoryRepository.findAll()).thenThrow(new RuntimeException());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> categoryService.findAllDto());
        assertEquals("Ошибка при получении всех Dto категорий", exception.getMessage());
        verify(categoryRepository, times(1)).findAll();
    }
}

