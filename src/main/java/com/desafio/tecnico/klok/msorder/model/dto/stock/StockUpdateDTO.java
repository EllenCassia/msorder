package com.desafio.tecnico.klok.msorder.model.dto.stock;

import jakarta.validation.constraints.Min;

public record StockUpdateDTO(
    
    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    int stockQuantity
) {}
