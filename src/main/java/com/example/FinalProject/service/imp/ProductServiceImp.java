package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.mapper.ProductMapper;
import com.example.FinalProject.model.Category;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.repository.CategoryRepository;
import com.example.FinalProject.repository.ImageRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImp implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImp.class);
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    public ProductServiceImp(ProductRepository productRepository, CategoryRepository categoryRepository,
                             ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
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

    @Transactional(readOnly = true)
    public List<ProductDto> findAllSortProducts(String categoryName, String sortBy, String order) {
        try {
            log.info("Получение всех отсортированных продуктов");
            List<Product> products = productRepository.findAll();

            if (categoryName != null) {
                Category category = categoryRepository.findByCategoryName(categoryName);
                products = products.stream()
                                   .filter(product -> product.getCategory().equals(category))
                                   .collect(Collectors.toList());
            }

            if (sortBy != null && order != null) {
                products = products.stream()
                                   .sorted((p1, p2) -> {
                                       int result = switch (sortBy) {
                                           case "price" -> Double.compare(p1.getPrice(), p2.getPrice());
                                           case "weightGrams" -> Integer.compare(p1.getWeightGrams(), p2.getWeightGrams());
                                           case "name" -> p1.getProductName().compareTo(p2.getProductName());
                                           default -> 0;
                                       };
                                       return "asc".equalsIgnoreCase(order) ? result : -result;
                                   })
                                   .collect(Collectors.toList());
            }

            return products.stream().map(ProductMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех продуктов", e);
            throw new RuntimeException("Ошибка при получении всех продуктов", e);
        }
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        try {
            imageRepository.deleteAll(imageRepository.findAllByProductProductId(productId));
            productRepository.deleteById(productId);
            log.info(String.format("Продукт с ID: %s успешно удален", productId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
            throw new RuntimeException(String.format("Ошибка при удалении продукта с ID: %s", productId), e);
        }
    }
}
