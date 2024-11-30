package com.arkam.microservices.order_service;

import com.arkam.microservices.order_service.client.InventoryClient;
import com.arkam.microservices.order_service.client.UserClient;
import com.arkam.microservices.order_service.model.Order;
import com.arkam.microservices.order_service.repository.OrderRepository;
import com.arkam.microservices.order_service.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceApplicationTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private InventoryClient inventoryClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder_Success() {
        // Arrange
        Order mockOrder = new Order();
        mockOrder.setUserId(1L);
        mockOrder.setSkuCode("ABC123");
        mockOrder.setQuantity(2);
        mockOrder.setPrice(BigDecimal.valueOf(100));

        when(userClient.checkUserId(1L)).thenReturn(true);
        when(inventoryClient.isInStock("ABC123", 2)).thenReturn(true);

        // Act
        orderService.placeOrder(mockOrder);

        // Assert
        verify(orderRepository, times(1)).save(mockOrder);
        verify(inventoryClient, times(1)).deleteByOrder("ABC123", 2);
    }

    @Test
    void testPlaceOrder_UserNotFound() {
        // Arrange
        Order mockOrder = new Order();
        mockOrder.setUserId(2L);
        mockOrder.setSkuCode("ABC123");
        mockOrder.setQuantity(2);

        when(userClient.checkUserId(2L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(mockOrder));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void testPlaceOrder_ProductNotInStock() {
        // Arrange
        Order mockOrder = new Order();
        mockOrder.setUserId(1L);
        mockOrder.setSkuCode("DEF456");
        mockOrder.setQuantity(5);

        when(userClient.checkUserId(1L)).thenReturn(true);
        when(inventoryClient.isInStock("DEF456", 5)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> orderService.placeOrder(mockOrder));
        verify(orderRepository, never()).save(any());
    }
}
