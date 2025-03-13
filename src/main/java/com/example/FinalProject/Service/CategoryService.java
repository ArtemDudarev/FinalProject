package com.example.FinalProject.Service;

import com.example.FinalProject.model.Category;
import com.example.FinalProject.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void saveCategory(Category category) {
        try {
            categoryRepository.save(category);
            log.info(String.format("Категория %s успешно сохранена", category.getCategoryName()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s категории", category.getCategoryName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s категории", category.getCategoryName()), e);
        }
    }

    @Transactional
    public void updateCategory(Category category) {
        try {
            if (categoryRepository.existsById(category.getCategoryId())) {
                categoryRepository.save(category);
                log.info(String.format("Категория %s обновлена", category.getCategoryName()));
            } else {
                log.info("Категория не найдена");
                throw new EntityNotFoundException("Категория не найдена");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s категории", category.getCategoryName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s категории", category.getCategoryName()), e);
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
    public List<Category> findAll() {
        try {
            log.info("Получение всех категорий");
            return categoryRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех категорий", e);
            throw new RuntimeException("Ошибка при получении всех категорий", e);
        }
    }

    @Transactional
    public void deleteCategory(UUID categoryId) {
        try {
            categoryRepository.deleteById(categoryId);
            log.info(String.format("Категория с ID: %s успешно удалена", categoryId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении категории с ID: %s", categoryId), e);
            throw new RuntimeException(String.format("Ошибка при удалении категории с ID: %s", categoryId), e);
        }
    }
}
