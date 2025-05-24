package com.desafio.tecnico.klok.msorder.model.dto.order;
import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemRequestDTO(
    UUID productId,
    int quantity,
    BigDecimal pricePerUnit
) {}
