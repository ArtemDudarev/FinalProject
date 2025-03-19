package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.mapper.CategoryMapper;
import com.example.FinalProject.model.Category;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.CategoryRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImp implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImp.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryServiceImp(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try {
            Category category = CategoryMapper.toEntity(categoryDto);
            categoryRepository.save(category);
            log.info(String.format("Категория %s успешно сохранена", category.getCategoryName()));
            return categoryDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s категории", categoryDto.getCategoryName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s категории", categoryDto.getCategoryName()), e);
        }
    }

    @Transactional
    public void updateCategory(UUID categoryId, CategoryDto categoryDto) {
        try {
            if (categoryRepository.existsById(categoryId)) {
                Category category = CategoryMapper.toEntity(categoryDto);
                category.setCategoryId(categoryId);
                categoryRepository.save(category);
                log.info(String.format("Категория %s обновлена", category.getCategoryName()));
            } else {
                log.info("Категория не найдена");
                throw new EntityNotFoundException("Категория не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s категории", categoryDto.getCategoryName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s категории", categoryDto.getCategoryName()), e);
        }
    }

    @Transactional(readOnly = true)
    public Category findCategoryById(UUID categoryId) {
        try {
            log.info(String.format("Получение категории по ID: %s", categoryId));
            return categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении категории по ID: %s", categoryId), e);
            throw new RuntimeException(String.format("Ошибка при получении категории по ID: %s", categoryId), e);
        }
    }

    @Transactional(readOnly = true)
    public CategoryDto findDtoCategoryById(UUID categoryId) {
        try {
            log.info(String.format("Получение категории по ID: %s", categoryId));
            return CategoryMapper.toDto(categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException("Категория не найдена")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении категории по ID: %s", categoryId), e);
            throw new RuntimeException(String.format("Ошибка при получении категории по ID: %s", categoryId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        try {
            log.info("Получение всех категорий");
            return categoryRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех категорий", e);
            throw new RuntimeException("Ошибка при получении всех категорий", e);
        }
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllDto() {
        try {
            log.info("Получение всех Dto категорий");
            List<Category> categories = categoryRepository.findAll();
            return categories.stream().map(CategoryMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto категорий", e);
            throw new RuntimeException("Ошибка при получении всех Dto категорий", e);
        }
    }

    @Transactional
    public void deleteCategory(UUID categoryId) {
        try {
            List<Product> products = productRepository.findAllByCategoryCategoryId(categoryId);
            Category noCategory = categoryRepository.findByCategoryName("No category");
            for (Product product : products) {
                product.setCategory(noCategory);
            }
            categoryRepository.deleteById(categoryId);
            log.info(String.format("Категория с ID: %s успешно удалена", categoryId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении категории с ID: %s", categoryId), e);
            throw new RuntimeException(String.format("Ошибка при удалении категории с ID: %s", categoryId), e);
        }
    }
}
