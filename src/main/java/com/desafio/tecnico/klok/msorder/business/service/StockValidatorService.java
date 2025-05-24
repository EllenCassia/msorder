package com.desafio.tecnico.klok.msorder.business.service;

import com.desafio.tecnico.klok.msorder.model.entity.Order;

public interface StockValidatorService {
    boolean validate(Order order);
    void reserveStock(Order order);
}
