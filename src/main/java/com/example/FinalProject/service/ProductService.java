package com.example.FinalProject.service;

import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.mapper.ProductMapper;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.CategoryRepository;
import com.example.FinalProject.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        try {
            Product product = ProductMapper.toEntity(productDto, categoryRepository);
            productRepository.save(product);
            log.info(String.format("Продукт %s успешно сохранен", productDto.getProductName()));
            return productDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s продукта", productDto.getProductName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s продукта", productDto.getProductName()), e);
        }
    }

    @Transactional
    public void updateProduct(UUID productId, ProductDto productDto) {
        try {
            if (productRepository.existsById(productId)) {
                Product product = ProductMapper.toEntity(productDto, categoryRepository);
                product.setProductId(productId);
                productRepository.save(product);
                log.info(String.format("Продукт %s обновлен", productDto.getProductName()));
            } else {
                log.info("Продукт не найден");
                throw new EntityNotFoundException("Продукт не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s продукта", productDto.getProductName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s продукта", productDto.getProductName()), e);
        }
    }

    @Transactional(readOnly = true)
    public Product findProductById(UUID productId) {
        try {
            log.info(String.format("Получение продукта по ID: %s", productId));
            return productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении продукта по ID: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при получении продукта по ID: %s", productId), e);
        }
    }

    @Transactional(readOnly = true)
    public ProductDto findProductDtoById(UUID productId) {
        try {
            log.info(String.format("Получение Dto продукта по ID: %s", productId));
            return ProductMapper.toDto(productRepository.findById(productId).orElseThrow(() -> new EntityNotFoundException("Продукт не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto продукта по ID: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto продукта по ID: %s", productId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        try {
            log.info("Получение всех продуктов");
            return productRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех продуктов", e);
            throw new RuntimeException("Ошибка при получении всех продуктов", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ProductDto> findAllDto() {
        try {
            log.info("Получение всех Dto продуктов");
            List<Product> products = productRepository.findAll();
            return products.stream().map(ProductMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto продуктов", e);
            throw new RuntimeException("Ошибка при получении всех Dto продуктов", e);
        }
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        try {
            productRepository.deleteById(productId);
            log.info(String.format("Продукт с ID: %s успешно удален", productId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
        }
    }
}
