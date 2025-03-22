package com.example.FinalProject.service;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.mapper.OrderMapper;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.OrderItem;
import com.example.FinalProject.model.PaymentMethod;
import com.example.FinalProject.model.Product;
import com.example.FinalProject.model.User;
import com.example.FinalProject.model.UserLoyaltyProgram;
import com.example.FinalProject.repository.LoyaltyProgramRepository;
import com.example.FinalProject.repository.OrderItemRepository;
import com.example.FinalProject.repository.OrderRepository;
import com.example.FinalProject.repository.PaymentMethodRepository;
import com.example.FinalProject.repository.ProductRepository;
import com.example.FinalProject.repository.UserLoyaltyProgramRepository;
import com.example.FinalProject.repository.UserRepository;
import com.example.FinalProject.service.imp.OrderServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @Mock
    private UserLoyaltyProgramRepository userLoyaltyProgramRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImp orderService;

    private OrderDto orderDto = OrderDto.builder().build();
    private Order order = new Order();
    private User user = new User();
    private PaymentMethod paymentMethod = new PaymentMethod();
    private Product product = new Product();
    private OrderItemDto orderItemDto = OrderItemDto.builder().build();
    private LoyaltyProgram loyaltyProgram = new LoyaltyProgram();
    private UserLoyaltyProgram userLoyaltyProgram = new UserLoyaltyProgram();
    private UUID orderId = UUID.randomUUID();
    private UUID userId = UUID.randomUUID();
    private UUID productId = UUID.randomUUID();

    @Test
    void testCreateOrderException() {
        when(orderMapper.toEntity(any(OrderDto.class), eq(userRepository), eq(paymentMethodRepository))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrder(orderDto));
        assertEquals(String.format("Ошибка при сохранении %s заказа", orderDto), exception.getMessage());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrderBasketUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderService.createOrderBasket(userId));
        assertEquals("Пользователь не найден", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testCreateOrderBasketException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentMethodRepository.findByMethodName("Cash")).thenReturn(paymentMethod);
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.createOrderBasket(userId));
        assertEquals(String.format("Ошибка при создании заказа для пользователя %s", userId), exception.getMessage());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testAddOrderItemOrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.addOrderItem(orderId, orderItemDto));
        assertEquals(String.format("Ошибка при добавлении элемента заказа в заказ %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testAddOrderItemProductNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(orderItemDto.getProductId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.addOrderItem(orderId, orderItemDto));
        assertEquals("Ошибка при добавлении элемента заказа в заказ " + orderId, exception.getMessage());
        verify(productRepository, times(1)).findById(orderItemDto.getProductId());
    }

    @Test
    void testAddOrderItemException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(productRepository.findById(orderItemDto.getProductId())).thenReturn(Optional.of(product));
        when(orderItemRepository.save(any(OrderItem.class))).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.addOrderItem(orderId, orderItemDto));
        assertEquals(String.format("Ошибка при добавлении элемента заказа в заказ %s", orderId), exception.getMessage());
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testConfirmOrderOrderNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderService.confirmOrder(orderId, PaymentMethodDto.builder()
                                                                                                                                                 .build()));
        assertEquals("Заказ не найден", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testConfirmOrderException() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(paymentMethodRepository.findByMethodName(anyString())).thenReturn(paymentMethod);
        when(userLoyaltyProgramRepository.findByUserUserId(any(UUID.class))).thenReturn(userLoyaltyProgram);
        when(loyaltyProgramRepository.findById(any(UUID.class))).thenReturn(Optional.of(loyaltyProgram));
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.confirmOrder(orderId, PaymentMethodDto.builder()
                                                                                                                                   .build()));
        assertEquals(String.format("Ошибка при подтверждении заказа %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testUpdateOrderNotFound() {
        when(orderRepository.existsById(orderId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderService.updateOrder(orderId, orderDto));
        assertEquals("Заказ не найден", exception.getMessage());
        verify(orderRepository, times(1)).existsById(orderId);
    }

    @Test
    void testUpdateOrderException() {
        when(orderRepository.existsById(orderId)).thenReturn(true);
        when(orderMapper.toEntity(any(OrderDto.class), eq(userRepository), eq(paymentMethodRepository))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.updateOrder(orderId, orderDto));
        assertEquals(String.format("Ошибка при обновлении %s заказа", orderId), exception.getMessage());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testFindOrderByIdNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> orderService.findOrderById(orderId));
        assertEquals("Заказ не найден", exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindOrderByIdException() {
        when(orderRepository.findById(orderId)).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.findOrderById(orderId));
        assertEquals(String.format("Ошибка при получении заказа по ID: %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindOrderDtoByIdNotFound() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.findOrderDtoById(orderId));
        assertEquals(String.format("Ошибка при получении Dto заказа по ID: %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindOrderDtoByIdException() {
        when(orderRepository.findById(orderId)).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.findOrderDtoById(orderId));
        assertEquals(String.format("Ошибка при получении Dto заказа по ID: %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void testFindAllOrdersException() {
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.findAll());
        assertEquals("Ошибка при получении всех заказов", exception.getMessage());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testFindAllOrderDtosException() {
        when(orderRepository.findAll()).thenThrow(new RuntimeException("Simulated exception"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.findAllDto());
        assertEquals("Ошибка при получении всех Dto заказов", exception.getMessage());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testDeleteOrderException() {
        doThrow(new RuntimeException("Simulated exception")).when(orderRepository).deleteById(orderId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.deleteOrder(orderId));
        assertEquals(String.format("Ошибка при удалении заказа с ID: %s", orderId), exception.getMessage());
        verify(orderRepository, times(1)).deleteById(orderId);
    }
}