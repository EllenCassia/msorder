package com.desafio.tecnico.klok.msorder.model.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.OrderStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    List<Order> findByClient_Id(UUID clientId);
    
    Page<Order> findByClient_Id(UUID clientId, Pageable pageable);
    
    List<Order> findByStatus(OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.client.id = :clientId AND o.status = :status")
    List<Order> findByClientIdAndStatus(@Param("clientId") UUID clientId, @Param("status") OrderStatus status);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersByDateRange(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);
    
    @Query("SELECT o FROM Order o WHERE o.inStock = false")
    List<Order> findOrdersOutOfStock();
    
    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :orderId")
    Optional<Order> findByIdWithItems(@Param("orderId") UUID orderId);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.client.id = :clientId")
    long countOrdersByClient(@Param("clientId") UUID clientId);
}


