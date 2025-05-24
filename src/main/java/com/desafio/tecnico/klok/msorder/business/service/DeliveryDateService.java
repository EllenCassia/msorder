package com.desafio.tecnico.klok.msorder.business.service;

import com.desafio.tecnico.klok.msorder.model.entity.Order;

import java.time.Instant;

public interface DeliveryDateService {
    void setDeliveryDate(Order order, Instant now);
}

