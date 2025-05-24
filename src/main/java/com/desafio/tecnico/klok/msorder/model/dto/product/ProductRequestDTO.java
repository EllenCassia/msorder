package com.desafio.tecnico.klok.msorder.model.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductRequestDTO(
    @NotBlank(message = "Nome do produto é obrigatório")
    String name,
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    BigDecimal price,
    
    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    int stockQuantity
) {}
