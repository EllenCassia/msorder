package com.desafio.tecnico.klok.msorder.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio.tecnico.klok.msorder.model.entity.OrderItem;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    
    List<OrderItem> findByOrderId(UUID orderId);
    
    List<OrderItem> findByProductId(UUID productId);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.quantity > oi.stockQuantity")
    List<OrderItem> findItemsOutOfStock();
    
    @Query("SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.productId = :productId")
    Long getTotalQuantityByProduct(@Param("productId") UUID productId);
}
