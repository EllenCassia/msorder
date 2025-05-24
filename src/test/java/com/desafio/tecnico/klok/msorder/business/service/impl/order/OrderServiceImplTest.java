package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderItemRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Client;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import com.desafio.tecnico.klok.msorder.model.repository.ClientRepository;
import com.desafio.tecnico.klok.msorder.model.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    private Client testClient;
    private Client vipClient;
    private Product testProduct;
    private Product premiumProduct;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    private void setupTestData() {
        testClient = createAndSaveClient("Test Client", "testclient@example.com", false);
        vipClient = createAndSaveClient("VIP Client", "vip@example.com", true);
        testProduct = createAndSaveProduct("Test Product", new BigDecimal("10.00"), 10);
        premiumProduct = createAndSaveProduct("Premium Product", new BigDecimal("100.00"), 10);
    }

    private Client createAndSaveClient(String name, String email, boolean isVip) {
        Client client = new Client(name, email, isVip);
        return clientRepository.save(client);
    }

    private Product createAndSaveProduct(String name, BigDecimal price, int stock) {
        Product product = new Product(name, price, stock);
        return productRepository.save(product);
    }

    private OrderItemRequestDTO createOrderItem(UUID productId, int quantity) {
        return new OrderItemRequestDTO(productId, quantity, BigDecimal.ZERO);
    }

    private OrderRequestDTO createOrderRequest(UUID clientId, OrderStatus status, List<OrderItemRequestDTO> items, int daysFromNow) {
        return new OrderRequestDTO(
                clientId,
                status,
                items,
                Instant.now().plus(daysFromNow, ChronoUnit.DAYS),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );
    }

    private OrderRequestDTO createSimpleOrderRequest(Client client, Product product, int quantity, OrderStatus status) {
        List<OrderItemRequestDTO> items = List.of(createOrderItem(product.getId(), quantity));
        return createOrderRequest(client.getId(), status, items, 3);
    }

    private OrderResponseDTO createTestOrder(Client client, Product product, int quantity, OrderStatus status) {
        OrderRequestDTO request = createSimpleOrderRequest(client, product, quantity, status);
        return orderService.createOrder(request);
    }

    @Test
    public void testCreateOrder_Success() {
        OrderItemRequestDTO item1 = createOrderItem(testProduct.getId(), 2);
        OrderItemRequestDTO item2 = createOrderItem(testProduct.getId(), 1);
        OrderRequestDTO orderRequest = createOrderRequest(
                testClient.getId(), 
                OrderStatus.PENDING, 
                List.of(item1, item2), 
                3
        );

        OrderResponseDTO response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(testClient.getId(), response.clientId());
        assertNotNull(response.id());
        assertEquals(2, response.items().size());
        assertEquals(OrderStatus.PENDING, response.status());
    }

    @Test
    public void testCreateOrder_InsufficientStock() {
        Product lowStockProduct = createAndSaveProduct("Low Stock Product", new BigDecimal("10.00"), 2);
        OrderRequestDTO orderRequest = createSimpleOrderRequest(testClient, lowStockProduct, 5, OrderStatus.PENDING);

        assertThrows(BusinessException.class, () -> orderService.createOrder(orderRequest));
    }

    @Test
    public void testCreateOrder_VipClient() {
        OrderRequestDTO orderRequest = createSimpleOrderRequest(vipClient, premiumProduct, 1, OrderStatus.PENDING);

        OrderResponseDTO response = orderService.createOrder(orderRequest);

        assertNotNull(response);
        assertEquals(vipClient.getId(), response.clientId());
        assertEquals(vipClient.getVip(), true);
    }

    @Test
    public void testFindById_Success() {
        OrderResponseDTO createdOrder = createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        OrderResponseDTO foundOrder = orderService.findById(createdOrder.id());

        assertNotNull(foundOrder);
        assertEquals(createdOrder.id(), foundOrder.id());
        assertEquals(testClient.getId(), foundOrder.clientId());
        assertEquals(1, foundOrder.items().size());
    }

    @Test
    public void testFindById_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(BusinessException.class, () -> orderService.findById(nonExistentId));
    }

    @Test
    public void testUpdateOrder_Success() {
        OrderResponseDTO createdOrder = createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        OrderRequestDTO updateRequest = createSimpleOrderRequest(testClient, testProduct, 3, OrderStatus.PROCESSING);

        OrderResponseDTO updatedOrder = orderService.updateOrder(createdOrder.id(), updateRequest);

        assertNotNull(updatedOrder);
        assertEquals(createdOrder.id(), updatedOrder.id());
        assertEquals(OrderStatus.PROCESSING, updatedOrder.status());
        assertEquals(1, updatedOrder.items().size());
    }

    @Test
    public void testUpdateOrder_NotFound() {
        UUID nonExistentId = UUID.randomUUID();
        OrderRequestDTO orderRequest = createSimpleOrderRequest(testClient, testProduct, 1, OrderStatus.PENDING);

        assertThrows(BusinessException.class, () -> orderService.updateOrder(nonExistentId, orderRequest));
    }

    @Test
    public void testUpdateOrderStatus_Success() {
        OrderResponseDTO createdOrder = createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(createdOrder.id(), OrderStatus.CONFIRMED);

        assertNotNull(updatedOrder);
        assertEquals(createdOrder.id(), updatedOrder.id());
        assertEquals(OrderStatus.CONFIRMED, updatedOrder.status());
    }

    @Test
    public void testDelete_Success() {
        OrderResponseDTO createdOrder = createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        orderService.delete(createdOrder.id());

        assertThrows(BusinessException.class, () -> orderService.findById(createdOrder.id()));
    }

    @Test
    public void testDelete_NotFound() {
        UUID nonExistentId = UUID.randomUUID();

        assertThrows(BusinessException.class, () -> orderService.delete(nonExistentId));
    }

    @Test
    public void testFindAll_Success() {
        createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);
        createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        List<OrderResponseDTO> orders = orderService.findAll();

        assertNotNull(orders);
        assertTrue(orders.size() >= 2);
    }

    @Test
    public void testFindByStatus_Success() {
        OrderResponseDTO createdOrder = createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        List<OrderResponseDTO> pendingOrders = orderService.findByStatus(OrderStatus.PENDING);

        assertNotNull(pendingOrders);
        assertTrue(pendingOrders.size() >= 1);
        assertTrue(pendingOrders.stream().anyMatch(order -> order.id().equals(createdOrder.id())));
    }

    @Test
    public void testCountOrdersByClient_Success() {
        createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);
        createTestOrder(testClient, testProduct, 1, OrderStatus.PENDING);

        long count = orderService.countOrdersByClient(testClient.getId());

        assertEquals(2L, count);
    }
}