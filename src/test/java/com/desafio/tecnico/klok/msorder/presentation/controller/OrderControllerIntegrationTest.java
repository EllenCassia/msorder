package com.desafio.tecnico.klok.msorder.presentation.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @AfterEach
        void tearDown() {
        }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrderById_NotFound() throws Exception {
        String nonExistentId = "123e4567-e89b-12d3-a456-426614174000";
        mockMvc.perform(get("/api/orders/{id}", nonExistentId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrdersByStatus() throws Exception {
        mockMvc.perform(get("/api/orders/status/{status}", "PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersByClientId() throws Exception {
        String clientId = "123e4567-e89b-12d3-a456-426614174000";
        mockMvc.perform(get("/api/orders/client/{clientId}", clientId))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersByClientIdAndStatus() throws Exception {
        String clientId = "123e4567-e89b-12d3-a456-426614174000";
        mockMvc.perform(get("/api/orders/client/{clientId}/status/{status}", clientId, "PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetOrdersOutOfStock() throws Exception {
        mockMvc.perform(get("/api/orders/out-of-stock"))
                .andExpect(status().isOk());
    }

    @Test
    public void testCountOrdersByClient() throws Exception {
        String clientId = "123e4567-e89b-12d3-a456-426614174000";
        mockMvc.perform(get("/api/orders/client/{clientId}/count", clientId))
                .andExpect(status().isOk());
    }

    private String createTestOrder() throws Exception {
        String orderJson = "{ \"clientId\": \"95620bd2-bef9-45fe-b6b5-4fc6cb332251\", \"status\": \"PENDING\", \"items\": [ { \"productId\": \"ae99cc77-68bc-4e15-bdfb-88c8ac5f9ee8\", \"quantity\": 1, \"pricePerUnit\": 10.0 } ], \"deliveryDate\": null, \"totalAmount\": 0, \"totalAmountWithDiscount\": 0 }";

        String response = mockMvc.perform(post("/api/orders")
                .contentType("application/json")
                .content(orderJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        com.fasterxml.jackson.databind.JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(response);
        return jsonNode.get("id").asText();
    }

    @Test
    public void testDeleteOrder() throws Exception {
        String orderId = createTestOrder();
        mockMvc.perform(delete("/api/orders/{id}", orderId))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateOrderStatus() throws Exception {
        String orderId = createTestOrder();
        mockMvc.perform(patch("/api/orders/{id}/status", orderId)
                .param("status", "PENDING"))
                .andExpect(status().isOk());
    }

}
