package com.example.FinalProject.controller;

import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.service.PaymentMethodService;
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
class PaymentMethodControllerTest {

    @Mock
    private PaymentMethodService paymentMethodService;

    @InjectMocks
    private PaymentMethodController paymentMethodController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private PaymentMethodDto paymentMethodDto;
    private UUID paymentMethodId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentMethodController).build();
        objectMapper = new ObjectMapper();
        paymentMethodId = UUID.randomUUID();
        paymentMethodDto = PaymentMethodDto.builder().methodName("Credit Card").build();
    }

    @Test
    void testCreatePaymentMethod() throws Exception {
        when(paymentMethodService.createPaymentMethod(any(PaymentMethodDto.class))).thenReturn(paymentMethodDto);

        mockMvc.perform(post("/api/payment-methods/create")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(paymentMethodDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.methodName").value("Credit Card"));

        verify(paymentMethodService, times(1)).createPaymentMethod(any(PaymentMethodDto.class));
    }

    @Test
    void testUpdatePaymentMethod() throws Exception {
        doNothing().when(paymentMethodService).updatePaymentMethod(eq(paymentMethodId), any(PaymentMethodDto.class));

        mockMvc.perform(put("/api/payment-methods/update/{id}", paymentMethodId)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(paymentMethodDto)))
               .andExpect(status().isNoContent());

        verify(paymentMethodService, times(1)).updatePaymentMethod(eq(paymentMethodId), any(PaymentMethodDto.class));
    }

    @Test
    void testGetPaymentMethodById() throws Exception {
        when(paymentMethodService.findPaymentMethodDtoById(paymentMethodId)).thenReturn(paymentMethodDto);

        mockMvc.perform(get("/api/payment-methods/{id}", paymentMethodId)
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.methodName").value("Credit Card"));

        verify(paymentMethodService, times(1)).findPaymentMethodDtoById(paymentMethodId);
    }

    @Test
    void testGetAllPaymentMethods() throws Exception {
        List<PaymentMethodDto> paymentMethodDtos = Arrays.asList(paymentMethodDto, PaymentMethodDto.builder().methodName("PayPal").build());
        when(paymentMethodService.findAllDto()).thenReturn(paymentMethodDtos);

        mockMvc.perform(get("/api/payment-methods")
                   .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$[0].methodName").value("Credit Card"))
               .andExpect(jsonPath("$[1].methodName").value("PayPal"));

        verify(paymentMethodService, times(1)).findAllDto();
    }

    @Test
    void testDeletePaymentMethod() throws Exception {
        doNothing().when(paymentMethodService).deletePaymentMethod(paymentMethodId);

        mockMvc.perform(delete("/api/payment-methods/delete/{id}", paymentMethodId))
               .andExpect(status().isNoContent());

        verify(paymentMethodService, times(1)).deletePaymentMethod(paymentMethodId);
    }
}