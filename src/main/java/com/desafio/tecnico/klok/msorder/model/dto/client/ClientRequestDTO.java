package com.desafio.tecnico.klok.msorder.model.dto.client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientRequestDTO(
    @NotBlank(message = "Nome é obrigatório")
    String name,
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ter formato válido")
    String email,
    
    @NotNull(message = "Status VIP é obrigatório")
    Boolean vip
) {}
