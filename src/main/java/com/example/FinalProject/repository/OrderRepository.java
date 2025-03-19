package com.example.FinalProject.repository;

import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.PaymentMethod;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByPaymentMethodPaymentMethodId(UUID paymentMethodId);
}
