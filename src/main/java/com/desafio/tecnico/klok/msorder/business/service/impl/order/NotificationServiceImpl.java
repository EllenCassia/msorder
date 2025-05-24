package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.desafio.tecnico.klok.msorder.business.service.NotificationService;
import com.desafio.tecnico.klok.msorder.model.entity.Order;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = Logger.getLogger(NotificationServiceImpl.class.getName());

    @Override
    public void notifyClient(Order order) {
        String email = order.getClient().getEmail();
        String message = order.isInStock()
                ? "Your order will be delivered soon."
                : "One or more items in your order are out of stock.";
        logger.info("Sending email to " + email + ": " + message);
    }
}
