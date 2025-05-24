package com.desafio.tecnico.klok.msorder.business.config;

import com.desafio.tecnico.klok.msorder.business.service.ProductService;
import com.desafio.tecnico.klok.msorder.model.dto.order.*;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.OrderItem;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;
import com.desafio.tecnico.klok.msorder.model.entity.Client;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final ProductService productService;

    public OrderMapper(ProductService productService) {
        this.productService = productService;
    }

    public OrderResponseDTO toDTO(Order order) {

        List<OrderItemResponseDTO> itemsDTO = order.getItems().stream()
            .map(this::toItemDTO)
            .collect(Collectors.toList());

        return new OrderResponseDTO(
            order.getId(),
            order.getClientId(),
            order.getClient().getName(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getTotalAmountWithDiscount(),
            order.getCreatedAt(),
            order.getDeliveryDate(),
            itemsDTO
        );
    }

    public OrderItemResponseDTO toItemDTO(OrderItem item) {
      
        String productName = productService.findNameById(item.getProductId());

        return new OrderItemResponseDTO(
            item.getId(),
            item.getProductId(),
            productName,
            item.getQuantity(),
            item.getPricePerUnit()
        );
    }

    public OrderItem fromItemDTO(OrderItemRequestDTO itemDTO) {
        OrderItem item = new OrderItem();
        item.setProductId(itemDTO.productId());
        item.setQuantity(itemDTO.quantity());
        item.setPricePerUnit(itemDTO.pricePerUnit());
        return item;
    }

    public Order fromDTO(OrderRequestDTO orderRequestDTO, Client client) {
        Order order = new Order();
        
        order.setClient(client);

        order.setStatus(orderRequestDTO.status() == null ? OrderStatus.PENDING : orderRequestDTO.status()); 
        order.setCreatedAt(Instant.now());
        order.setDeliveryDate(orderRequestDTO.deliveryDate());
        order.setTotalAmount(orderRequestDTO.totalAmount());
        order.setTotalAmountWithDiscount(orderRequestDTO.totalAmountWithDiscount());
        order.setInStock(false); 

        List<OrderItem> items = orderRequestDTO.items().stream()
            .map(this::fromItemDTO)
            .collect(Collectors.toList());
        
        items.forEach(item -> item.setOrder(order));
        order.setItems(items);
        
        return order;
    }

}