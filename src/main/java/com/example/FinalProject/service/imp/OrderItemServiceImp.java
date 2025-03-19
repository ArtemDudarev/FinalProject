package com.example.FinalProject.service.imp;

import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.mapper.OrderItemMapper;
import com.example.FinalProject.model.OrderItem;
import com.example.FinalProject.repository.OrderItemRepository;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.OrderItemService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderItemServiceImp implements OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemServiceImp.class);
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderItemServiceImp(OrderItemRepository orderItemRepository, ProductRepository productRepository,
                               OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        try {
            OrderItem orderItem = OrderItemMapper.toEntity(orderItemDto, orderRepository, productRepository);
            orderItemRepository.save(orderItem);
            log.info(String.format("Элемент заказа %s успешно сохранен", orderItemDto));
            return orderItemDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s элемента заказа", orderItemDto), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s элемента заказа", orderItemDto), e);
        }
    }

    @Transactional
    public void updateOrderItem(UUID orderItemId, OrderItemDto orderItemDto) {
        try {
            if (orderItemRepository.existsById(orderItemId)) {
                OrderItem orderItem = OrderItemMapper.toEntity(orderItemDto, orderRepository, productRepository);
                orderItem.setOrderItemId(orderItemId);
                orderItemRepository.save(orderItem);
                log.info(String.format("Элемент заказа %s обновлен", orderItemId));
            } else {
                log.info("Элемент заказа не найден");
                throw new EntityNotFoundException("Элемент заказа не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s элемента заказа", orderItemId), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s элемента заказа", orderItemId), e);
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
    public OrderItemDto findOrderItemDtoById(UUID orderItemId) {
        try {
            log.info(String.format("Получение Dto элемента заказа по ID: %s", orderItemId));
            return OrderItemMapper.toDto(orderItemRepository.findById(orderItemId).orElseThrow(() -> new EntityNotFoundException("Элемент заказа не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto элемента заказа по ID: %s", orderItemId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto элемента заказа по ID: %s", orderItemId), e);
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

    @Transactional(readOnly = true)
    public List<OrderItemDto> findAllDto() {
        try {
            log.info("Получение всех Dto элементов заказов");
            List<OrderItem> orderItems = orderItemRepository.findAll();
            return orderItems.stream().map(OrderItemMapper::toDto).toList();
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
