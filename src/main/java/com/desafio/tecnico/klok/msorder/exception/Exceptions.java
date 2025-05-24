package com.desafio.tecnico.klok.msorder.exception;

import java.util.UUID;

public class Exceptions {

    public static BusinessException clientNotFound(UUID id) {
        return new BusinessException("Cliente não encontrado para o ID: " + id, "CLIENT_NOT_FOUND");
    }

    public static BusinessException clientNotFound(String email) {
        return new BusinessException("Cliente não encontrado para o email: " + email, "CLIENT_NOT_FOUND");
    }

    public static BusinessException orderNotFound(UUID id) {
        return new BusinessException("Pedido não encontrado para o ID: " + id, "ORDER_NOT_FOUND");
    }

    public static BusinessException productNotFound(UUID id) {
        return new BusinessException("Produto não encontrado para o ID: " + id, "PRODUCT_NOT_FOUND");
    }

    public static BusinessException insufficientStock(UUID productId, int requested, int available) {
        return new BusinessException(
            String.format("Estoque insuficiente para o produto %s. Solicitado: %d, Disponível: %d",
                productId, requested, available),
            "INSUFFICIENT_STOCK"
        );
    }

    public static BusinessException invalidOrder(String message) {
        return new BusinessException(message, "INVALID_ORDER");
    }

    public static BusinessException duplicateClient(String email) {
        return new BusinessException("Cliente já existe com o email: " + email, "DUPLICATE_CLIENT");
    }

    public static BusinessException authError(String message) {
        return new BusinessException(message, "AUTHENTICATION_ERROR");
    }

    public static BusinessException externalService(String serviceName, String message) {
        return new BusinessException("Erro no serviço externo " + serviceName + ": " + message, "EXTERNAL_SERVICE_ERROR");
    }
    public static BusinessException notFound(String entityName, String identifier) {
        return new BusinessException(entityName + " não encontrado para o identificador: " + identifier, "NOT_FOUND");
    }

}

