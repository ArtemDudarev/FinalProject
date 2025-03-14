package com.example.FinalProject.service;

import com.example.FinalProject.model.Order;
import com.example.FinalProject.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void createOrder(Order order) {
        try {
            orderRepository.save(order);
            log.info(String.format("Заказ %s успешно сохранен", order.getOrderId()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s заказа", order.getOrderId()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s заказа", order.getOrderId()), e);
        }
    }

    @Transactional
    public void updateOrder(Order order) {
        try {
            if (orderRepository.existsById(order.getOrderId())) {
                orderRepository.save(order);
                log.info(String.format("Заказ %s обновлен", order.getOrderId()));
            } else {
                log.info("Заказ не найден");
                throw new EntityNotFoundException("Заказ не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s заказа", order.getOrderId()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s заказа", order.getOrderId()), e);
        }
    }

    @Transactional(readOnly = true)
    public Order findOrderById(UUID orderId) {
        try {
            log.info(String.format("Получение заказа по ID: %s", orderId));
            return orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении заказа по ID: %s", orderId), e);
            throw new RuntimeException(String.format("Ошибка при получении заказа по ID: %s", orderId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        try {
            log.info("Получение всех заказов");
            return orderRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех заказов", e);
            throw new RuntimeException("Ошибка при получении всех заказов", e);
        }
    }

    @Transactional
    public void deleteOrder(UUID orderId) {
        try {
            orderRepository.deleteById(orderId);
            log.info(String.format("Заказ с ID: %s успешно удален", orderId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении заказа с ID: %s", orderId), e);
            throw new RuntimeException(String.format("Ошибка при удалении заказа с ID: %s", orderId), e);
        }
    }
}
