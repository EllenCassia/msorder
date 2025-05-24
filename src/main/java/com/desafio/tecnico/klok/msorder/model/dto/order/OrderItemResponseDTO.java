package com.desafio.tecnico.klok.msorder.model.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponseDTO(
    UUID id,
    UUID productId,
    String productName,
    int quantity,
    BigDecimal pricePerUnit
) {}



