package com.desafio.tecnico.klok.msorder.business.service;

import com.desafio.tecnico.klok.msorder.model.entity.Order;

public interface OrderCalculationService {
    void calculateTotal(Order order);
}
