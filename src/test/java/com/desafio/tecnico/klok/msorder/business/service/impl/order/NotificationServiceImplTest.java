package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.model.entity.Client;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class NotificationServiceImplTest {

    @Test
    public void testNotifyClient() {
        NotificationServiceImpl notificationService = new NotificationServiceImpl();

        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setEmail("test@example.com");

        Order order = new Order();
        order.setClient(client);
        order.setInStock(true);

        notificationService.notifyClient(order);

        order.setInStock(false);
        notificationService.notifyClient(order);
    }
}
