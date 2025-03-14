package com.example.FinalProject.service;

import com.example.FinalProject.model.OrderItem;
import com.example.FinalProject.repository.OrderItemRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);
    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public void createOrderItem(OrderItem orderItem) {
        try {
            orderItemRepository.save(orderItem);
            log.info(String.format("Элемент заказа %s успешно сохранен", orderItem.getOrderItemId()));
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s элемента заказа", orderItem.getOrderItemId()), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s элемента заказа", orderItem.getOrderItemId()), e);
        }
    }

    @Transactional
    public void updateOrderItem(OrderItem orderItem) {
        try {
            if (orderItemRepository.existsById(orderItem.getOrderItemId())) {
                orderItemRepository.save(orderItem);
                log.info(String.format("Элемент заказа %s обновлен", orderItem.getOrderItemId()));
            } else {
                log.info("Элемент заказа не найден");
                throw new EntityNotFoundException("Элемент заказа не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s элемента заказа", orderItem.getOrderItemId()), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s элемента заказа", orderItem.getOrderItemId()), e);
        }
    }

    @Transactional(readOnly = true)
    public OrderItem findOrderItemById(UUID orderItemId) {
        try {
            log.info(String.format("Получение элемента заказа по ID: %s", orderItemId));
            return orderItemRepository.findById(orderItemId).orElseThrow(() -> new EntityNotFoundException("Элемент заказа не найден"));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении элемента заказа по ID: %s", orderItemId), e);
            throw new RuntimeException(String.format("Ошибка при получении элемента заказа по ID: %s", orderItemId), e);
        }
    }

    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        try {
            log.info("Получение всех элементов заказов");
            return orderItemRepository.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении всех элементов заказов", e);
            throw new RuntimeException("Ошибка при получении всех элементов заказов", e);
        }
    }

    @Transactional
    public void deleteOrderItem(UUID orderItemId) {
        try {
            orderItemRepository.deleteById(orderItemId);
            log.info(String.format("Элемент заказа с ID: %s успешно удален", orderItemId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при удалении элемента заказа с ID: %s", orderItemId), e);
            throw new RuntimeException(String.format("Ошибка при удалении элемента заказа с ID: %s", orderItemId), e);
        }
    }
}
