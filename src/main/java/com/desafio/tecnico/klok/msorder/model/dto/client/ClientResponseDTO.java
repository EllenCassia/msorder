package com.desafio.tecnico.klok.msorder.model.dto.client;

import java.util.UUID;

public record ClientResponseDTO(
    UUID id,
    String name,
    String email,
    boolean vip
) {}

