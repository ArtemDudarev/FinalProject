package com.example.FinalProject.repository;

import com.example.FinalProject.model.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {}
