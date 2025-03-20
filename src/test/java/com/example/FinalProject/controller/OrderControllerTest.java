package com.example.FinalProject.controller;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private OrderDto orderDto;
    private OrderItemDto orderItemDto;
    private PaymentMethodDto paymentMethodDto;
    private UUID orderId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
        orderId = UUID.randomUUID();
        userId = UUID.randomUUID();
        orderDto = OrderDto.builder().userId(userId).build();
        orderItemDto = OrderItemDto.builder().productId(UUID.randomUUID()).quantity(1).pricePerItem(10.0).build();
        paymentMethodDto = PaymentMethodDto.builder().methodName("Cash").build();
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrderBasket(userId)).thenReturn(orderDto);

        mockMvc.perform(post("/api/orders/create")
                   .param("userId", userId.toString())
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(orderService, times(1)).createOrderBasket(userId);
    }

    @Test
    void testAddOrderItem() throws Exception {
        doNothing().when(orderService).addOrderItem(eq(orderId), any(OrderItemDto.class));

        mockMvc.perform(post("/api/orders/{orderId}/add-item", orderId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(orderItemDto)))
               .andExpect(status().isNoContent());

        verify(orderService, times(1)).addOrderItem(eq(orderId), any(OrderItemDto.class));
    }

    @Test
    void testConfirmOrder() throws Exception {
        doNothing().when(orderService).confirmOrder(eq(orderId), any(PaymentMethodDto.class));

        mockMvc.perform(put("/api/orders/{orderId}/confirm", orderId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(paymentMethodDto)))
               .andExpect(status().isNoContent());

        verify(orderService, times(1)).confirmOrder(eq(orderId), any(PaymentMethodDto.class));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.findOrderDtoById(orderId)).thenReturn(orderDto);

        mockMvc.perform(get("/api/orders/order/{id}", orderId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.userId").value(userId.toString()));

        verify(orderService, times(1)).findOrderDtoById(orderId);
    }

    @Test
    void testGetAllOrders() throws Exception {
        List<OrderDto> orderDtos = Arrays.asList(orderDto, OrderDto.builder().userId(UUID.randomUUID()).build());
        when(orderService.findAllDto()).thenReturn(orderDtos);

        mockMvc.perform(get("/api/orders")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].userId").value(userId.toString()));

        verify(orderService, times(1)).findAllDto();
    }

    @Test
    void testDeleteOrder() throws Exception {
        doNothing().when(orderService).deleteOrder(orderId);

        mockMvc.perform(delete("/api/orders/delete/{id}", orderId))
               .andExpect(status().isNoContent());

        verify(orderService, times(1)).deleteOrder(orderId);
    }
}
