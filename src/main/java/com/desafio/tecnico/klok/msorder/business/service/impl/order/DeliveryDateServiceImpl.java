package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import org.springframework.stereotype.Service;

import com.desafio.tecnico.klok.msorder.business.service.DeliveryDateService;
import com.desafio.tecnico.klok.msorder.model.entity.Order;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class DeliveryDateServiceImpl implements DeliveryDateService {

    @Override
    public void setDeliveryDate(Order order, Instant now) {
        if (order.isInStock()) {
            ZonedDateTime deliveryDate = now.atZone(ZoneId.systemDefault()).plusDays(3);
            order.setDeliveryDate(deliveryDate.toInstant());
        } else {
            order.setDeliveryDate(null);
        }
    }
}

