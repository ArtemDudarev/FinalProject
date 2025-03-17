package com.example.FinalProject.service;

import com.example.FinalProject.dto.OrderDto;
import com.example.FinalProject.dto.OrderItemDto;
import com.example.FinalProject.dto.PaymentMethodDto;
import com.example.FinalProject.mapper.OrderMapper;
import com.example.FinalProject.model.LoyaltyProgram;
import com.example.FinalProject.model.Order;
import com.example.FinalProject.model.OrderItem;
import com.example.FinalProject.model.OrderStatus;
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
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Date;
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
    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final LoyaltyProgramRepository loyaltyProgramRepository;
    private final UserLoyaltyProgramRepository userLoyaltyProgramRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        PaymentMethodRepository paymentMethodRepository, ProductRepository productRepository,
                        OrderItemRepository orderItemRepository, LoyaltyProgramRepository loyaltyProgramRepository,
                        UserLoyaltyProgramRepository userLoyaltyProgramRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.loyaltyProgramRepository = loyaltyProgramRepository;
        this.userLoyaltyProgramRepository = userLoyaltyProgramRepository;
    }

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {
        try {
            Order order = OrderMapper.toEntity(orderDto, userRepository, paymentMethodRepository);
            order.setOrderStatus(OrderStatus.PROCESSING.toString());
            order.setOrderDate(LocalDateTime.now());
            orderRepository.save(order);
            log.info(String.format("Заказ %s успешно сохранен", orderDto));
            return orderDto;
        } catch (Exception e) {
            log.error(String.format("Ошибка при сохранении %s заказа", orderDto), e);
            throw new RuntimeException(String.format("Ошибка при сохранении %s заказа", orderDto), e);
        }
    }

    @Transactional
    public OrderDto createOrderBasket(UUID userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            PaymentMethod defaultMethod = paymentMethodRepository.findByMethodName("Cash");
            Order order = Order.builder()
                               .user(user)
                               .orderDate(LocalDateTime.now())
                               .totalAmount(0.0)
                               .paymentMethod(defaultMethod)
                               .orderStatus(OrderStatus.PROCESSING.toString())
                               .build();
            orderRepository.save(order);
            log.info(String.format("Заказ %s успешно создан", order.getOrderId()));
            return OrderMapper.toDto(order);
        } catch (Exception e) {
            log.error(String.format("Ошибка при создании заказа для пользователя %s", userId), e);
            throw new RuntimeException(String.format("Ошибка при создании заказа для пользователя %s", userId), e);
        }
    }

    @Transactional
    public void addOrderItem(UUID orderId, OrderItemDto orderItemDto) {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
            Product product = productRepository.findById(orderItemDto.getProductId()).orElseThrow(() -> new EntityNotFoundException("Продукт не найден"));

            OrderItem orderItem = OrderItem.builder()
                                           .order(order)
                                           .product(product)
                                           .quantity(orderItemDto.getQuantity())
                                           .pricePerItem(orderItemDto.getPricePerItem())
                                           .build();
            orderItemRepository.save(orderItem);
            order.setTotalAmount(order.getTotalAmount() + (orderItem.getPricePerItem() * orderItem.getQuantity()));
            log.info(String.format("Элемент заказа %s успешно добавлен в заказ %s", orderItem.getOrderItemId(), orderId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при добавлении элемента заказа в заказ %s", orderId), e);
            throw new RuntimeException(String.format("Ошибка при добавлении элемента заказа в заказ %s", orderId), e);
        }
    }

    @Transactional
    public void confirmOrder(UUID orderId, PaymentMethodDto paymentMethodDto) {
        try {
            PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName(paymentMethodDto.getMethodName());

            Order order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Заказ не найден"));
            order.setPaymentMethod(paymentMethod);
            order.setTotalAmount(order.getTotalAmount()*(1 - getDiscount(order)));
            order.setOrderStatus(OrderStatus.CONFIRM.toString());
            order.setOrderDate(LocalDateTime.now());
            orderRepository.save(order);
            log.info(String.format("Заказ %s подтвержден", orderId));
        } catch (Exception e) {
            log.error(String.format("Ошибка при подтверждении заказа %s", orderId), e);
            throw new RuntimeException(String.format("Ошибка при подтверждении заказа %s", orderId), e);
        }
    }

    @Transactional
    public Double getDiscount(Order order){
        UUID userId = order.getUser().getUserId();
        UserLoyaltyProgram userLoyaltyProgram = userLoyaltyProgramRepository.findByUserUserId(userId);
        LoyaltyProgram loyaltyProgram = loyaltyProgramRepository.findById(
            userLoyaltyProgram.getLoyaltyProgram().getLoyaltyProgramId()).orElseThrow(
            () -> new EntityNotFoundException("Программа лояльности не найден"));
        return loyaltyProgram.getDiscountPercentage();
    }

    @Transactional
    public void updateOrder(UUID orderId, OrderDto orderDto) {
        try {
            if (orderRepository.existsById(orderId)) {
                Order order = OrderMapper.toEntity(orderDto, userRepository, paymentMethodRepository);
                order.setOrderId(orderId);
                orderRepository.save(order);
                log.info(String.format("Заказ %s обновлен", orderId));
            } else {
                log.info("Заказ не найден");
                throw new EntityNotFoundException("Заказ не найден");
            }
        } catch (Exception e) {
            log.error(String.format("Ошибка при обновлении %s заказа", orderId), e);
            throw new RuntimeException(String.format("Ошибка при обновлении %s заказа", orderId), e);
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
    public OrderDto findOrderDtoById(UUID orderId) {
        try {
            log.info(String.format("Получение Dto заказа по ID: %s", orderId));
            return OrderMapper.toDto(orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Заказ не найден")));
        } catch (Exception e) {
            log.error(String.format("Ошибка при получении Dto заказа по ID: %s", orderId), e);
            throw new RuntimeException(String.format("Ошибка при получении Dto заказа по ID: %s", orderId), e);
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

    @Transactional(readOnly = true)
    public List<OrderDto> findAllDto() {
        try {
            log.info("Получение всех Dto заказов");
            List<Order> orders = orderRepository.findAll();
            return orders.stream().map(OrderMapper::toDto).toList();
        } catch (Exception e) {
            log.error("Ошибка при получении всех Dto заказов", e);
            throw new RuntimeException("Ошибка при получении всех Dto заказов", e);
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
