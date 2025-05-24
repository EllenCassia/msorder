package com.desafio.tecnico.klok.msorder.business.service.strategy;

import java.math.BigDecimal;

import com.desafio.tecnico.klok.msorder.model.entity.Order;

public interface DiscountStrategy {
    BigDecimal applyDiscount(Order order);
}
