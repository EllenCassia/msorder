package com.desafio.tecnico.klok.msorder.business.service.impl.client;

import com.desafio.tecnico.klok.msorder.business.service.impl.client.ClienteServiceImpl;
import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Client;
import com.desafio.tecnico.klok.msorder.model.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClienteById_Success() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);
        client.setName("John Doe");
        client.setEmail("john@example.com");
        client.setVip(false);

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        ClientResponseDTO response = clienteService.getClienteById(id);

        assertNotNull(response);
        assertEquals(id, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john@example.com", response.email());
        assertFalse(response.vip());
    }

    @Test
    public void testGetClienteById_NotFound() {
        UUID id = UUID.randomUUID();
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            clienteService.getClienteById(id);
        });

        assertTrue(exception.getMessage().contains("não encontrado"));
    }

    @Test
    public void testCreateCliente_Success() {
        String name = "Jane Doe";
        String email = "jane@example.com";
        boolean vip = true;

        when(clientRepository.existsByEmail(email)).thenReturn(false);

        Client savedClient = new Client();
        savedClient.setId(UUID.randomUUID());
        savedClient.setName(name);
        savedClient.setEmail(email);
        savedClient.setVip(vip);

        when(clientRepository.save(ArgumentMatchers.any(Client.class))).thenReturn(savedClient);

        ClientResponseDTO response = clienteService.createCliente(name, email, vip);

        assertNotNull(response);
        assertEquals(name, response.name());
        assertEquals(email, response.email());
        assertTrue(response.vip());
    }

    @Test
    public void testCreateCliente_DuplicateEmail() {
        String email = "duplicate@example.com";
        when(clientRepository.existsByEmail(email)).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            clienteService.createCliente("Name", email, false);
        });

        assertTrue(exception.getMessage().contains("duplicate"));
    }

    @Test
    public void testGetAllVipClients() {
        
        Client vipClient = new Client();
        vipClient.setId(UUID.randomUUID());
        vipClient.setName("VIP Client");
        vipClient.setEmail("vip@example.com");
        vipClient.setVip(true);

        when(clientRepository.findAllVipClients()).thenReturn(Collections.singletonList(vipClient));

        List<ClientResponseDTO> vipClients = clienteService.getAllVipClients();

        assertNotNull(vipClients);
        assertEquals(1, vipClients.size());
        assertTrue(vipClients.get(0).vip());
    }

    @Test
    public void testCountVipClients() {
        when(clientRepository.countVipClients()).thenReturn(5L);

        long count = clienteService.countVipClients();

        assertEquals(5L, count);
    }

    @Test
    public void testValidateClient_Success() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        when(clientRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> clienteService.validateClient(client));
    }

    @Test
    public void testValidateClient_NullClient() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            clienteService.validateClient(null);
        });

        System.out.println("Exception message: " + exception.getMessage());

        assertTrue(exception.getMessage().contains("Cliente não encontrado"));
    }

    @Test
    public void testValidateClient_NotExists() {
        UUID id = UUID.randomUUID();
        Client client = new Client();
        client.setId(id);

        when(clientRepository.existsById(id)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            clienteService.validateClient(client);
        });

        assertTrue(exception.getMessage().contains("já existe"));
    }
}
