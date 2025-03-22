package com.example.FinalProject.controller;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.service.ImageService;
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
class ImageControllerTest {

    @Mock
    private ImageService imageService;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ImageDto imageDto;
    private UUID imageId;
    private UUID productId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
        objectMapper = new ObjectMapper();
        imageId = UUID.randomUUID();
        productId = UUID.randomUUID();
        imageDto = ImageDto.builder().imageUrl("test.jpg").build();
    }

    @Test
    void testCreateImage() throws Exception {
        when(imageService.createImage(any(ImageDto.class))).thenReturn(imageDto);

        mockMvc.perform(post("/api/images/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(imageDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.imageUrl").value("test.jpg"));

        verify(imageService, times(1)).createImage(any(ImageDto.class));
    }

    @Test
    void testCreateImageForProduct() throws Exception {
        when(imageService.createImageForProduct(eq(productId), any(ImageDto.class))).thenReturn(imageDto);

        mockMvc.perform(post("/api/images/create/{productId}", productId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(imageDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.imageUrl").value("test.jpg"));

        verify(imageService, times(1)).createImageForProduct(eq(productId), any(ImageDto.class));
    }

    @Test
    void testUpdateImage() throws Exception {
        doNothing().when(imageService).updateImage(eq(imageId), any(ImageDto.class));

        mockMvc.perform(put("/api/images/update/{id}", imageId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(imageDto)))
               .andExpect(status().isNoContent());

        verify(imageService, times(1)).updateImage(eq(imageId), any(ImageDto.class));
    }

    @Test
    void testGetImageById() throws Exception {
        when(imageService.findImageDtoById(imageId)).thenReturn(imageDto);

        mockMvc.perform(get("/api/images/{id}", imageId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.imageUrl").value("test.jpg"));

        verify(imageService, times(1)).findImageDtoById(imageId);
    }

    @Test
    void testGetImageByProductId() throws Exception {
        List<ImageDto> imageDtos = Arrays.asList(imageDto, ImageDto.builder().imageUrl("another.jpg").build());
        when(imageService.findAllByProductProductId(productId)).thenReturn(imageDtos);

        mockMvc.perform(get("/api/images/by-product/{productId}", productId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].imageUrl").value("test.jpg"))
               .andExpect(jsonPath("$[1].imageUrl").value("another.jpg"));

        verify(imageService, times(1)).findAllByProductProductId(productId);
    }

    @Test
    void testGetAllImages() throws Exception {
        List<ImageDto> imageDtos = Arrays.asList(imageDto, ImageDto.builder().imageUrl("another.jpg").build());
        when(imageService.findAllDto()).thenReturn(imageDtos);

        mockMvc.perform(get("/api/images")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].imageUrl").value("test.jpg"))
               .andExpect(jsonPath("$[1].imageUrl").value("another.jpg"));

        verify(imageService, times(1)).findAllDto();
    }

    @Test
    void testDeleteImage() throws Exception {
        doNothing().when(imageService).deleteImage(imageId);

        mockMvc.perform(delete("/api/images/delete/{id}", imageId))
               .andExpect(status().isNoContent());

        verify(imageService, times(1)).deleteImage(imageId);
    }
}