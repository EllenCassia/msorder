package com.desafio.tecnico.klok.msorder.presentation.controller;

import com.desafio.tecnico.klok.msorder.business.service.OrderService;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Criar um novo pedido
     */
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO created = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Buscar pedido por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> findById(@PathVariable UUID id) {
        OrderResponseDTO order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    /**
     * Listar todos os pedidos
     */
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> findAll() {
        List<OrderResponseDTO> orders = orderService.findAll();
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos por cliente com paginação
     */
    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<OrderResponseDTO>> findByClientId(
            @PathVariable UUID clientId,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<OrderResponseDTO> orders = orderService.findByClientId(clientId, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos por status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> findByStatus(@PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.findByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos por cliente e status
     */
    @GetMapping("/client/{clientId}/status/{status}")
    public ResponseEntity<List<OrderResponseDTO>> findByClientIdAndStatus(
            @PathVariable UUID clientId,
            @PathVariable OrderStatus status) {
        List<OrderResponseDTO> orders = orderService.findByClientIdAndStatus(clientId, status);
        return ResponseEntity.ok(orders);
    }

    /**
     * Buscar pedidos fora de estoque
     */
    @GetMapping("/out-of-stock")
    public ResponseEntity<List<OrderResponseDTO>> findOrdersOutOfStock() {
        List<OrderResponseDTO> orders = orderService.findOrdersOutOfStock();
        return ResponseEntity.ok(orders);
    }

    /**
     * Contar pedidos por cliente
     */
    @GetMapping("/client/{clientId}/count")
    public ResponseEntity<Long> countOrdersByClient(@PathVariable UUID clientId) {
        long count = orderService.countOrdersByClient(clientId);
        return ResponseEntity.ok(count);
    }

    /**
     * Atualizar pedido
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO updated = orderService.updateOrder(id, orderRequestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletar pedido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Atualizar status do pedido
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam OrderStatus status) {
        OrderResponseDTO updated = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updated);
    }


}