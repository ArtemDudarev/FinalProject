package com.example.FinalProject.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.repository.OrderItemRepository;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.service.imp.OrderItemServiceImp;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderItemServiceImp orderItemService;

    private UUID orderItemId = UUID.randomUUID();
    private OrderItemDto orderItemDto = new OrderItemDto();

    @Test
    void testCreateOrderItemException() {
        assertThrows(RuntimeException.class, () -> orderItemService.createOrderItem(orderItemDto));
        verify(orderItemRepository, never()).findById(any());
    }

    @Test
    void testUpdateOrderItemException() {
        when(orderItemRepository.existsById(orderItemId)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> orderItemService.updateOrderItem(orderItemId, orderItemDto));

        verify(orderItemRepository, times(1)).existsById(orderItemId);
        verify(orderItemRepository, never()).findById(any());
    }

    @Test
    void testFindOrderItemDtoByIdException() {
        when(orderItemRepository.findById(orderItemId)).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> orderItemService.findOrderItemDtoById(orderItemId));

        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).findById(any());
    }

    @Test
    void testFindAllDtoException() {
        when(orderItemRepository.findAll()).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> orderItemService.findAllDto());

        verify(productRepository, never()).findById(any());
        verify(orderRepository, never()).findById(any());
    }
}