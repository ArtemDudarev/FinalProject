package com.example.FinalProject.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageDto {
    private String imageUrl;
    private UUID productId;
}