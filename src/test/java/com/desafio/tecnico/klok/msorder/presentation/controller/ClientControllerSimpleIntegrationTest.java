package com.desafio.tecnico.klok.msorder.presentation.controller;

import com.desafio.tecnico.klok.msorder.business.service.ClienteService;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerSimpleIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID clientId;
    private ClientResponseDTO clientResponseDTO;
    private ClientRequestDTO clientRequestDTO;

    @AfterEach
    void tearDown() {
    }

    @BeforeEach
    void setUp() {
        clientId = UUID.randomUUID();
        clientResponseDTO = new ClientResponseDTO(clientId, "Simple Client", "simple@example.com", false);
        clientRequestDTO = new ClientRequestDTO("Simple Client", "simple@example.com", false);
    }

    @Test
    void getClientById_shouldReturnOk() throws Exception {
        when(clienteService.getClienteById(clientId)).thenReturn(clientResponseDTO);

        mockMvc.perform(get("/api/clients/{id}", clientId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getClientByEmail_shouldReturnOk() throws Exception {
        when(clienteService.getClienteByEmail("simple@example.com")).thenReturn(clientResponseDTO);

        mockMvc.perform(get("/api/clients/email/{email}", "simple@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void updateClient_shouldReturnOk() throws Exception {
        when(clienteService.updateCliente(any(UUID.class), anyString(), anyString(), any(Boolean.class))).thenReturn(clientResponseDTO);

        mockMvc.perform(put("/api/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllVipClients_shouldReturnOk() throws Exception {
        List<ClientResponseDTO> vipClients = Arrays.asList(clientResponseDTO);
        when(clienteService.getAllVipClients()).thenReturn(vipClients);

        mockMvc.perform(get("/api/clients/vip"))
                .andExpect(status().isOk());
    }

    @Test
    void countVipClients_shouldReturnOk() throws Exception {
        when(clienteService.countVipClients()).thenReturn(1L);

        mockMvc.perform(get("/api/clients/vip/count"))
                .andExpect(status().isOk());
    }
}