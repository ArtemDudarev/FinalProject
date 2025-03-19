package com.example.FinalProject.controller;

import com.example.FinalProject.dto.ImageDto;
import com.example.FinalProject.service.ImageService;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/images")
@AllArgsConstructor
public class ImageController {

    private ImageService imageService;

    @PostMapping("/create")
    public ResponseEntity<ImageDto> createImage(@RequestBody ImageDto imageDto) {
        ImageDto createdImage = imageService.createImage(imageDto);
        return ResponseEntity.ok(createdImage);
    }

    @PostMapping("/create/{productId}")
    public ResponseEntity<ImageDto> createImageForProduct(@PathVariable UUID productId, @RequestBody ImageDto imageDto) {
        ImageDto createdImage = imageService.createImageForProduct(productId, imageDto);
        return ResponseEntity.ok(createdImage);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateImage(@PathVariable UUID id, @RequestBody ImageDto imageDto) {
        imageService.updateImage(id, imageDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable UUID id) {
        ImageDto imageDto = imageService.findImageDtoById(id);
        return ResponseEntity.ok(imageDto);
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<List<ImageDto>> getImageByProductId(@PathVariable UUID productId) {
        List<ImageDto> images = imageService.findAllByProductProductId(productId);
        return ResponseEntity.ok(images);
    }

    @GetMapping
    public ResponseEntity<List<ImageDto>> getAllImages() {
        List<ImageDto> images = imageService.findAllDto();
        return ResponseEntity.ok(images);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable UUID id) {
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }
}
