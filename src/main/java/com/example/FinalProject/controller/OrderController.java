package com.example.FinalProject.controller;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.service.OrderService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(@RequestParam UUID userId) {
        OrderDto createdOrder = orderService.createOrderBasket(userId);
        return ResponseEntity.ok(createdOrder);
    }

    @PostMapping("/{orderId}/add-item")
    public ResponseEntity<Void> addOrderItem(@PathVariable UUID orderId, @RequestBody OrderItemDto orderItemDto) {
        orderService.addOrderItem(orderId, orderItemDto);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Void> confirmOrder(@PathVariable UUID orderId, @RequestBody PaymentMethodDto paymentMethodDto) {
        orderService.confirmOrder(orderId, paymentMethodDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        OrderDto orderDto = orderService.findOrderDtoById(id);
        return ResponseEntity.ok(orderDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.findAllDto();
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
