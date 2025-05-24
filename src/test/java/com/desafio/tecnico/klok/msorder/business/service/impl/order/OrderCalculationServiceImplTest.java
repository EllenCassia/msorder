package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.business.service.ProductService;
import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.OrderItem;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderCalculationServiceImplTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderCalculationServiceImpl orderCalculationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateTotal_WithPricePerUnit() {
        OrderItem item1 = new OrderItem(UUID.randomUUID(), 2, 10, new BigDecimal("10.00"));
        OrderItem item2 = new OrderItem(UUID.randomUUID(), 1, 5, new BigDecimal("20.00"));

        Order order = new Order();
        order.setItems(List.of(item1, item2));

        orderCalculationService.calculateTotal(order);

        assertEquals(new BigDecimal("40.00"), order.getTotalAmount());
        verifyNoInteractions(productService);
    }

    @Test
    public void testCalculateTotal_WithNullPricePerUnit() {
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        OrderItem item1 = new OrderItem(productId1, 2, 10, null);
        OrderItem item2 = new OrderItem(productId2, 1, 5, null);

        Product product1 = new Product("Product1", new BigDecimal("10.00"), 10);
        Product product2 = new Product("Product2", new BigDecimal("20.00"), 5);

        when(productService.getProductById(productId1)).thenReturn(product1);
        when(productService.getProductById(productId2)).thenReturn(product2);

        Order order = new Order();
        order.setItems(List.of(item1, item2));

        orderCalculationService.calculateTotal(order);

        assertEquals(new BigDecimal("40.00"), order.getTotalAmount());
        verify(productService, times(1)).getProductById(productId1);
        verify(productService, times(1)).getProductById(productId2);
    }

    @Test
    public void testCalculateTotal_ProductNotFound() {
        UUID productId = UUID.randomUUID();

        OrderItem item = new OrderItem(productId, 1, 5, null);

        when(productService.getProductById(productId)).thenReturn(null);

        Order order = new Order();
        order.setItems(List.of(item));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderCalculationService.calculateTotal(order);
        });

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
        verify(productService, times(1)).getProductById(productId);
    }
}
