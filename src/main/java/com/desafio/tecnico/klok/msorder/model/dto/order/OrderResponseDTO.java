package com.desafio.tecnico.klok.msorder.model.dto.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;

public record OrderResponseDTO(
    UUID id,
    UUID clientId,
    String clientName,
    OrderStatus status,
    BigDecimal totalAmount,
    BigDecimal totalAmountWithDiscount,
    Instant createdAt,
    Instant deliveryDate,
    List<OrderItemResponseDTO> items
) {}

