package com.desafio.tecnico.klok.msorder.model.dto.order;

import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderRequestDTO(
    UUID clientId,
    OrderStatus status,
    List<OrderItemRequestDTO> items,
    Instant deliveryDate,
    BigDecimal totalAmount,
    BigDecimal totalAmountWithDiscount
) {}
