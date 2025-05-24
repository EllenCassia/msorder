package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.business.config.OrderMapper;
import com.desafio.tecnico.klok.msorder.business.service.*;
import com.desafio.tecnico.klok.msorder.business.service.impl.factory.DiscountStrategyFactory;
import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.exception.Exceptions;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.order.OrderResponseDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Client;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;
import com.desafio.tecnico.klok.msorder.model.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ClienteService clienteService;
    private final DiscountStrategyFactory discountStrategyFactory;
    private final StockValidatorService stockValidatorService;
    private final DeliveryDateService deliveryDateService;
    private final NotificationService notificationService;
    private final OrderMapper orderMapper;
    private final OrderCalculationService orderCalculationService;

    public OrderServiceImpl(OrderRepository orderRepository,
                            ClienteService clienteService,
                            DiscountStrategyFactory discountStrategyFactory,
                            StockValidatorService stockValidatorService,
                            DeliveryDateService deliveryDateService,
                            NotificationService notificationService,
                            OrderMapper orderMapper,
                            OrderCalculationService orderCalculationService) {
        this.orderRepository = orderRepository;
        this.clienteService = clienteService;
        this.discountStrategyFactory = discountStrategyFactory;
        this.stockValidatorService = stockValidatorService;
        this.deliveryDateService = deliveryDateService;
        this.notificationService = notificationService;
        this.orderMapper = orderMapper;
        this.orderCalculationService = orderCalculationService;
    }

    private void executeOrderProcessing(Order order) { 
        
        clienteService.validateClient(order.getClient());
        orderCalculationService.calculateTotal(order);

        var client = clienteService.getClienteById(order.getClientId());
        var strategy = discountStrategyFactory.getStrategy(client);

        BigDecimal totalComDesconto = strategy.applyDiscount(order);
        order.setTotalAmountWithDiscount(totalComDesconto);

        boolean inStock = stockValidatorService.validate(order);
       
        if (!inStock) {
            throw Exceptions.insufficientStock(order.getId(), 1, 0);
        }

        stockValidatorService.reserveStock(order);
        order.setInStock(inStock);

        deliveryDateService.setDeliveryDate(order, Instant.now());
        notificationService.notifyClient(order);

    }

    @Override
    public OrderResponseDTO findById(UUID id) {
        try {
            Order order = orderRepository.findByIdWithItems(id)
                    .orElseThrow(() -> Exceptions.orderNotFound(id));
            return orderMapper.toDTO(order);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao buscar pedido pelo ID: " + e.getMessage(), "FIND_ORDER_ERROR");
        }
    }

    @Override
    public List<OrderResponseDTO> findAll() {
        try {
            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar pedidos: " + e.getMessage(), "LIST_ORDER_ERROR");
        }
    }

    @Override
    public Page<OrderResponseDTO> findByClientId(UUID clientId, Pageable pageable) {
        try {
            Page<Order> ordersPage = orderRepository.findByClient_Id(clientId, pageable);
            return ordersPage.map(orderMapper::toDTO);
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar pedidos por cliente: " + e.getMessage(), "LIST_ORDER_BY_CLIENT_ERROR");
        }
    }

    @Override
    public List<OrderResponseDTO> findByStatus(OrderStatus status) {
        try {
            List<Order> orders = orderRepository.findByStatus(status);
            return orders.stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar pedidos por status: " + e.getMessage(), "LIST_ORDER_BY_STATUS_ERROR");
        }
    }

    @Override
    public List<OrderResponseDTO> findByClientIdAndStatus(UUID clientId, OrderStatus status) {
        try {
            List<Order> orders = orderRepository.findByClientIdAndStatus(clientId, status);
            return orders.stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar pedidos por cliente e status: " + e.getMessage(), "LIST_ORDER_BY_CLIENT_STATUS_ERROR");
        }
    }

    @Override
    public List<OrderResponseDTO> findOrdersOutOfStock() {
        try {
            List<Order> orders = orderRepository.findOrdersOutOfStock();
            return orders.stream()
                    .map(orderMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessException("Erro ao listar pedidos fora de estoque: " + e.getMessage(), "LIST_ORDER_OUT_OF_STOCK_ERROR");
        }
    }

    @Override
    public long countOrdersByClient(UUID clientId) {
        try {
            return orderRepository.countOrdersByClient(clientId);
        } catch (Exception e) {
            throw new BusinessException("Erro ao contar pedidos por cliente: " + e.getMessage(), "COUNT_ORDER_BY_CLIENT_ERROR");
        }
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        try {
            Client client = clienteService.findClientEntityById(orderRequestDTO.clientId());

            Order order = orderMapper.fromDTO(orderRequestDTO, client);

            executeOrderProcessing(order);

            Order saved = orderRepository.save(order);

            return orderMapper.toDTO(saved);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao criar pedido: " + e.getMessage(), "CREATE_ORDER_ERROR");
        }
    }
    @Override
    public OrderResponseDTO updateOrder(UUID id, OrderRequestDTO orderRequestDTO) {
        try {
            Order existing = orderRepository.findByIdWithItems(id)
                    .orElseThrow(() -> Exceptions.orderNotFound(id));

            Client client = clienteService.findClientEntityById(orderRequestDTO.clientId());

            Order updatedOrder = orderMapper.fromDTO(orderRequestDTO, client);
            
            updatedOrder.setId(existing.getId());
            updatedOrder.setCreatedAt(existing.getCreatedAt());
            updatedOrder.setVersion(existing.getVersion()); 
            
            Order saved = orderRepository.save(updatedOrder);
            return orderMapper.toDTO(saved);
            
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao atualizar pedido: " + e.getMessage(), "UPDATE_ORDER_ERROR");
        }
    }


    @Override
    public void delete(UUID id) {
        try {
            Order existing = orderRepository.findByIdWithItems(id)
                    .orElseThrow(() -> Exceptions.orderNotFound(id));
            orderRepository.delete(existing);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao deletar pedido: " + e.getMessage(), "DELETE_ORDER_ERROR");
        }
    }

    @Override
    public OrderResponseDTO updateOrderStatus(UUID id, OrderStatus status) {
        try {
            Order order = orderRepository.findByIdWithItems(id)
                    .orElseThrow(() -> Exceptions.orderNotFound(id));
            order.setStatus(status);
            Order saved = orderRepository.save(order);
            return orderMapper.toDTO(saved);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException("Erro ao atualizar status do pedido: " + e.getMessage(), "UPDATE_ORDER_STATUS_ERROR");
        }
    }

}