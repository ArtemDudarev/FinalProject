package com.example.FinalProject.controller;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.service.PaymentMethodService;
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
@RequestMapping("/api/payment-methods")
@AllArgsConstructor
public class PaymentMethodController {

    private PaymentMethodService paymentMethodService;

    @PostMapping("/create")
    public ResponseEntity<PaymentMethodDto> createPaymentMethod(@RequestBody PaymentMethodDto paymentMethodDto) {
        PaymentMethodDto createdPaymentMethod = paymentMethodService.createPaymentMethod(paymentMethodDto);
        return ResponseEntity.ok(createdPaymentMethod);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updatePaymentMethod(@PathVariable UUID id, @RequestBody PaymentMethodDto paymentMethodDto) {
        paymentMethodService.updatePaymentMethod(id, paymentMethodDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> getPaymentMethodById(@PathVariable UUID id) {
        PaymentMethodDto paymentMethodDto = paymentMethodService.findPaymentMethodDtoById(id);
        return ResponseEntity.ok(paymentMethodDto);
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getAllPaymentMethods() {
        List<PaymentMethodDto> paymentMethods = paymentMethodService.findAllDto();
        return ResponseEntity.ok(paymentMethods);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable UUID id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
