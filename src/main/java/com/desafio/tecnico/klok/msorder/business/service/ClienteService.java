package com.desafio.tecnico.klok.msorder.business.service;

import java.util.List;
import java.util.UUID;

import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Client;

public interface ClienteService {

    ClientResponseDTO getClienteById(UUID clienteId);

    ClientResponseDTO getClienteByEmail(String email);

    ClientResponseDTO createCliente(String name, String email, boolean vip);

    ClientResponseDTO updateCliente(UUID clienteId, String name, String email, boolean vip);

    List<ClientResponseDTO> getAllVipClients();

    long countVipClients();

    void validateClient(Client client);

    Client findClientEntityById(UUID clienteId);

}

