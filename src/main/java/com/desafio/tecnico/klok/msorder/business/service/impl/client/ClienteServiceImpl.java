package com.desafio.tecnico.klok.msorder.business.service.impl.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.tecnico.klok.msorder.business.service.ClienteService;
import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.exception.Exceptions;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Client;
import com.desafio.tecnico.klok.msorder.model.repository.ClientRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClientRepository clientRepository;

    @Override
    public ClientResponseDTO getClienteById(UUID clienteId) {
        log.debug("Buscando cliente por ID: {}", clienteId);
        Client cliente = clientRepository.findById(clienteId)
                .orElseThrow(() -> Exceptions.clientNotFound(clienteId));
        return mapToResponseDTO(cliente);
    }

    @Override
    public ClientResponseDTO getClienteByEmail(String email) {
        log.debug("Buscando cliente por email: {}", email);
        Client cliente = clientRepository.findByEmail(email)
                .orElseThrow(() -> Exceptions.clientNotFound(email));
        return mapToResponseDTO(cliente);
    }

    @Override
    @Transactional
    public ClientResponseDTO createCliente(String name, String email, boolean vip) {
        log.debug("Criando novo cliente: {}", email);
        if (clientRepository.existsByEmail(email)) {
            throw Exceptions.duplicateClient(email);
        }

        Client cliente = new Client();
        cliente.setName(name);
        cliente.setEmail(email);
        cliente.setVip(vip);

        Client savedCliente = clientRepository.save(cliente);
        log.info("Cliente criado com sucesso: {}", savedCliente.getId());
        return mapToResponseDTO(savedCliente);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateCliente(UUID clienteId, String name, String email, boolean vip) {
        log.debug("Atualizando cliente: {}", clienteId);
        Client cliente = clientRepository.findById(clienteId)
                .orElseThrow(() -> Exceptions.clientNotFound(clienteId));

        if (!cliente.getEmail().equals(email) && clientRepository.existsByEmail(email)) {
            throw Exceptions.duplicateClient(email);
        }

        cliente.setName(name);
        cliente.setEmail(email);
        cliente.setVip(vip);

        Client updatedCliente = clientRepository.save(cliente);
        log.info("Cliente atualizado com sucesso: {}", updatedCliente.getId());

        return mapToResponseDTO(updatedCliente);
    }

    @Override
    public List<ClientResponseDTO> getAllVipClients() {
        log.debug("Buscando todos os clientes VIP");
        return clientRepository.findAllVipClients().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public long countVipClients() {
        return clientRepository.countVipClients();
    }

    private ClientResponseDTO mapToResponseDTO(Client cliente) {
        return new ClientResponseDTO(
                cliente.getId(),
                cliente.getName(),
                cliente.getEmail(),
                cliente.getVip()
        );
    }
    @Override
    public void validateClient(Client client) {

        if (client == null) {
            throw Exceptions.clientNotFound((java.util.UUID) null);
        }

        if (client.getId() == null) {
            throw Exceptions.clientNotFound((java.util.UUID) null);
        }
        
        boolean exists = clientRepository.existsById(client.getId());

        if (!exists) {
            throw Exceptions.duplicateClient(client.getEmail());
        }
    }

    @Override
    public Client findClientEntityById(UUID clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com ID: " + clientId, "CLIENT_NOT_FOUND"));
    }

}
