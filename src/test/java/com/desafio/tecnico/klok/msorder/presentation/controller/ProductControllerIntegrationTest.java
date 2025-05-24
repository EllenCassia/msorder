package com.desafio.tecnico.klok.msorder.presentation.controller;

import com.desafio.tecnico.klok.msorder.model.dto.product.ProductRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.stock.StockUpdateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID existingProductId;

    @BeforeEach
    public void setup() throws Exception {
        
        ProductRequestDTO productRequest = new ProductRequestDTO("Test Product", new BigDecimal("100.00"), 50);
        String response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        existingProductId = objectMapper.readTree(response).get("id").asText().isEmpty() ? null : UUID.fromString(objectMapper.readTree(response).get("id").asText());
    }

    @Test
    public void testGetAllActiveProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductById_Success() throws Exception {
        mockMvc.perform(get("/api/products/{id}", existingProductId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductById_NotFound() throws Exception {
        mockMvc.perform(get("/api/products/{id}", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAvailableProducts() throws Exception {
        mockMvc.perform(get("/api/products/available"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateProduct_Success() throws Exception {
        ProductRequestDTO productRequest = new ProductRequestDTO("New Product", new BigDecimal("50.00"), 20);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testUpdateProductStock_Success() throws Exception {
        StockUpdateDTO stockUpdate = new StockUpdateDTO(100);
        mockMvc.perform(put("/api/products/{id}/stock", existingProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductsWithLowStock_DefaultThreshold() throws Exception {
        mockMvc.perform(get("/api/products/low-stock"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductsWithLowStock_CustomThreshold() throws Exception {
        mockMvc.perform(get("/api/products/low-stock")
                .param("threshold", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetProductNameById_Success() throws Exception {
        mockMvc.perform(get("/api/products/{id}/name", existingProductId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    public void testGetProductNameById_NotFound() throws Exception {
        mockMvc.perform(get("/api/products/{id}/name", "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isBadRequest());
    }
}
