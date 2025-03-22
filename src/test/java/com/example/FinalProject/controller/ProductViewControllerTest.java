package com.example.FinalProject.controller;

import com.example.FinalProject.dto.ProductDto;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductViewControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductViewController productViewController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductDto productDto;
    private Product product;
    private UUID productId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productViewController).build();
        objectMapper = new ObjectMapper();
        productId = UUID.randomUUID();
        productDto = ProductDto.builder().productName("Test Product").build();
        product = new Product();
        product.setProductId(productId);
        product.setProductName("Test Product");
    }

    @Test
    void testCreateProduct() throws Exception {
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        mockMvc.perform(post("/api/products/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(productDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.productName").value("Test Product"));

        verify(productService, times(1)).createProduct(any(ProductDto.class));
    }

    @Test
    void testUpdateProduct() throws Exception {
        doNothing().when(productService).updateProduct(eq(productId), any(ProductDto.class));

        mockMvc.perform(put("/api/products/update/{id}", productId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(productDto)))
               .andExpect(status().isNoContent());

        verify(productService, times(1)).updateProduct(eq(productId), any(ProductDto.class));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.findProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", productId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.productName").value("Test Product"));

        verify(productService, times(1)).findProductById(productId);
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductDto> productDtos = Arrays.asList(productDto, ProductDto.builder().productName("Another Product").build());
        when(productService.findAllSortProducts(any(), any(), any())).thenReturn(productDtos);

        mockMvc.perform(get("/api/products")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].productName").value("Test Product"))
               .andExpect(jsonPath("$[1].productName").value("Another Product"));

        verify(productService, times(1)).findAllSortProducts(any(), any(), any());
    }

    @Test
    void testGetAllProductsWithParams() throws Exception {
        List<ProductDto> productDtos = Arrays.asList(productDto);
        when(productService.findAllSortProducts(eq("CategoryTest"), eq("price"), eq("asc"))).thenReturn(productDtos);

        mockMvc.perform(get("/api/products")
                   .param("categoryName", "CategoryTest")
                   .param("sortBy", "price")
                   .param("order", "asc")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].productName").value("Test Product"));

        verify(productService, times(1)).findAllSortProducts(eq("CategoryTest"), eq("price"), eq("asc"));
    }

    @Test
    void testDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(productId);

        mockMvc.perform(delete("/api/products/delete/{id}", productId))
               .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(productId);
    }
}