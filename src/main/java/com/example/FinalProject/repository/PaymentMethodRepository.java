package com.example.FinalProject.repository;

import com.example.FinalProject.model.PaymentMethod;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, UUID> {

    PaymentMethod findByMethodName(String methodName);
}
