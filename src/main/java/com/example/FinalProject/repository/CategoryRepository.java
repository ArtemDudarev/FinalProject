package com.example.FinalProject.repository;

import com.example.FinalProject.model.Category;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Category findByCategoryName(String categoryName);
}
