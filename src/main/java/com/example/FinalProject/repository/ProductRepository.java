package com.example.FinalProject.repository;

import com.example.FinalProject.model.Product;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    List<Product> findAllByCategoryCategoryId(UUID categoryId);
}
