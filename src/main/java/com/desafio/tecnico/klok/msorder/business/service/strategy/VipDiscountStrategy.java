package com.desafio.tecnico.klok.msorder.business.service.strategy;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.desafio.tecnico.klok.msorder.model.entity.Order;

@Component
public class VipDiscountStrategy implements DiscountStrategy {

    @Override
    public BigDecimal applyDiscount(Order order) {
       
        BigDecimal total = order.getTotalAmount();
        return total.multiply(new BigDecimal("0.90"));
    }

}
