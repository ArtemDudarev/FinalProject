package com.example.FinalProject.controller;

import com.example.FinalProject.dto.CategoryDto;
import com.example.FinalProject.service.CategoryService;
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
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private CategoryDto categoryDto;
    private UUID categoryId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
        categoryId = UUID.randomUUID();
        categoryDto = CategoryDto.builder().categoryName("Test Category").build();
    }

    @Test
    void testCreateCategory() throws Exception {
        when(categoryService.createCategory(any(CategoryDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/api/categories/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(categoryDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.categoryName").value("Test Category"));

        verify(categoryService, times(1)).createCategory(any(CategoryDto.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        doNothing().when(categoryService).updateCategory(eq(categoryId), any(CategoryDto.class));

        mockMvc.perform(put("/api/categories/update/{id}", categoryId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(categoryDto)))
               .andExpect(status().isNoContent());

        verify(categoryService, times(1)).updateCategory(eq(categoryId), any(CategoryDto.class));
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.findDtoCategoryById(categoryId)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/categories/{id}", categoryId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.categoryName").value("Test Category"));

        verify(categoryService, times(1)).findDtoCategoryById(categoryId);
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<CategoryDto> categoryDtos = Arrays.asList(categoryDto, CategoryDto.builder().categoryName("Another Category").build());
        when(categoryService.findAllDto()).thenReturn(categoryDtos);

        mockMvc.perform(get("/api/categories")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].categoryName").value("Test Category"))
               .andExpect(jsonPath("$[1].categoryName").value("Another Category"));

        verify(categoryService, times(1)).findAllDto();
    }

    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/categories/delete/{id}", categoryId))
               .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}
