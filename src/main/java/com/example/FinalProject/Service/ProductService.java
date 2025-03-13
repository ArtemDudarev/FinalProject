package com.example.FinalProject.Service;

import com.example.FinalProject.model.Product;
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

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void saveProduct(Product product) {
        try {
            productRepository.save(product);
            log.info(String.format("Продукт %s успешно сохранен", product.getProductName()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s продукта", product.getProductName()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s продукта", product.getProductName()), e);
        }
    }

    @Transactional
    public void updateProduct(Product product) {
        try {
            if (productRepository.existsById(product.getProductId())) {
                productRepository.save(product);
                log.info(String.format("Продукт %s обновлен", product.getProductName()));
            } else {
                log.info("Продукт не найден");
                throw new EntityNotFoundException("Продукт не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s продукта", product.getProductName()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s продукта", product.getProductName()), e);
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
    public List<Product> findAll() {
        try {
            log.info("Получение всех продуктов");
            return productRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех продуктов", e);
            throw new RuntimeException("Ошибка при получении всех продуктов", e);
        }
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        try {
            log.info(String.format("Удаление продукта с ID: %s", productId));
            productRepository.deleteById(productId);
            log.info(String.format("Продукт с ID: %s успешно удален", productId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
        }
    }
}
