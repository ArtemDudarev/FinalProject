package com.example.FinalProject.service;

import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.model.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    ProductDto createProduct(ProductDto productDto);

    void updateProduct(UUID productId, ProductDto productDto);

    Product findProductById(UUID productId);

    ProductDto findProductDtoById(UUID productId);

    List<Product> findAll();

    List<ProductDto> findAllDto();

    List<ProductDto> findAllSortProducts(String categoryName, String sortBy, String order);

    void deleteProduct(UUID productId);
}
