package com.desafio.tecnico.klok.msorder.presentation.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desafio.tecnico.klok.msorder.business.service.ClienteService;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "*") 
@RequiredArgsConstructor
public class ClientController {

    private final ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable UUID id) {
        try {
            ClientResponseDTO client = clienteService.getClienteById(id);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            log.error("Erro ao buscar cliente {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponseDTO> getClientByEmail(@PathVariable String email) {
        try {
            ClientResponseDTO client = clienteService.getClienteByEmail(email);
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            log.error("Erro ao buscar cliente por email {}: {}", email, e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request) {
        try {
            ClientResponseDTO client = clienteService.createCliente(
                    request.name(), 
                    request.email(), 
                    request.vip()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(client);
        } catch (Exception e) {
            log.error("Erro ao criar cliente: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientRequestDTO request) {
        try {
            ClientResponseDTO client = clienteService.updateCliente(
                    id, 
                    request.name(), 
                    request.email(), 
                    request.vip()
            );
            return ResponseEntity.ok(client);
        } catch (Exception e) {
            log.error("Erro ao atualizar cliente {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/vip")
    public ResponseEntity<List<ClientResponseDTO>> getAllVipClients() {
        try {
            List<ClientResponseDTO> vipClients = clienteService.getAllVipClients();
            return ResponseEntity.ok(vipClients);
        } catch (Exception e) {
            log.error("Erro ao buscar clientes VIP: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/vip/count")
    public ResponseEntity<Long> countVipClients() {
        try {
            long count = clienteService.countVipClients();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Erro ao contar clientes VIP: {}", e.getMessage());
            throw e;
        }
    }
}