package com.desafio.tecnico.klok.msorder.business.service.impl.product;

import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import com.desafio.tecnico.klok.msorder.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProductById_Success() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Test Product", BigDecimal.TEN, 10);
        product.setId(productId);

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals(productId, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    public void testGetProductById_NotFound() {
        UUID productId = UUID.randomUUID();

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BusinessException.class, () -> {
            productService.getProductById(productId);
        });

        assertTrue(exception.getMessage().contains("Produto não encontrado"));
    }

    @Test
    public void testGetAllActiveProducts() {
        Product product = new Product("Active Product", BigDecimal.TEN, 5);
        when(productRepository.findByActiveTrue()).thenReturn(List.of(product));

        List<Product> products = productService.getAllActiveProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Active Product", products.get(0).getName());
    }

    @Test
    public void testCreateProduct() {
        Product product = new Product("New Product", BigDecimal.valueOf(15.5), 20);
        when(productRepository.save(ArgumentMatchers.any(Product.class))).thenReturn(product);

        Product created = productService.createProduct("New Product", BigDecimal.valueOf(15.5), 20);

        assertNotNull(created);
        assertEquals("New Product", created.getName());
        assertEquals(BigDecimal.valueOf(15.5), created.getPrice());
        assertEquals(20, created.getStockQuantity());
    }

    @Test
    public void testUpdateProductStock() {
        UUID productId = UUID.randomUUID();
        Product product = new Product("Stock Product", BigDecimal.TEN, 10);
        product.setId(productId);

        when(productRepository.findByIdAndActiveTrue(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);

        Product updated = productService.updateProductStock(productId, 15);

        assertNotNull(updated);
        assertEquals(15, updated.getStockQuantity());
    }

    @Test
    public void testGetProductsWithLowStock() {
        Product product = new Product("Low Stock Product", BigDecimal.TEN, 2);
        when(productRepository.findProductsWithLowStock(5)).thenReturn(List.of(product));

        List<Product> products = productService.getProductsWithLowStock(5);

        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Low Stock Product", products.get(0).getName());
    }
}
