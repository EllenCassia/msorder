package com.desafio.tecnico.klok.msorder.business.service;

import com.desafio.tecnico.klok.msorder.model.dto.order.OrderRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponseDTO findById(UUID id);

    List<OrderResponseDTO> findAll();

    Page<OrderResponseDTO> findByClientId(UUID clientId, Pageable pageable);

    List<OrderResponseDTO> findByStatus(OrderStatus status);

    List<OrderResponseDTO> findByClientIdAndStatus(UUID clientId, OrderStatus status);

    List<OrderResponseDTO> findOrdersOutOfStock();

    long countOrdersByClient(UUID clientId);

    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);

    OrderResponseDTO updateOrder(UUID id, OrderRequestDTO orderRequestDTO);

    void delete(UUID id);

    OrderResponseDTO updateOrderStatus(UUID id, OrderStatus status);

}
