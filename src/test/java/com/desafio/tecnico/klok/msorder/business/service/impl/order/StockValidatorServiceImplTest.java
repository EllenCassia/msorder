package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.OrderItem;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import com.desafio.tecnico.klok.msorder.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StockValidatorServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private StockValidatorServiceImpl stockValidatorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidate_SufficientStock() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(productId, 2, 5, null);
        Order order = new Order();
        order.setItems(List.of(item));

        Product product = new Product("Product", null, 10);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        boolean result = stockValidatorService.validate(order);

        assertTrue(result);
    }

    @Test
    public void testValidate_InsufficientStock() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(productId, 6, 5, null);
        Order order = new Order();
        order.setItems(List.of(item));

        Product product = new Product("Product", null, 5);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        boolean result = stockValidatorService.validate(order);

        assertFalse(result);
    }

    @Test
    public void testValidate_ProductNotFound() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(productId, 1, 5, null);
        Order order = new Order();
        order.setItems(List.of(item));

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> stockValidatorService.validate(order));
    }

    @Test
    public void testReserveStock_Success() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(productId, 2, 5, null);
        Order order = new Order();
        order.setItems(List.of(item));

        Product product = new Product("Product", null, 10);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        stockValidatorService.reserveStock(order);

        assertEquals(8, product.getStockQuantity());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    public void testReserveStock_InsufficientStock() {
        UUID productId = UUID.randomUUID();
        OrderItem item = new OrderItem(productId, 6, 5, null);
        Order order = new Order();
        order.setItems(List.of(item));

        Product product = new Product("Product", null, 5);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(BusinessException.class, () -> stockValidatorService.reserveStock(order));
    }
}
