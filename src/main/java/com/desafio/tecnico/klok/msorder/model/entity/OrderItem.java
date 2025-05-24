package com.desafio.tecnico.klok.msorder.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import com.desafio.tecnico.klok.msorder.exception.Exceptions;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {

     @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price_per_unit", nullable = false, precision = 19, scale = 2)
    private BigDecimal pricePerUnit;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Version
    @Column(name = "version")
    private Long version;

    public OrderItem(UUID productId, int quantity, int stockQuantity, BigDecimal pricePerUnit) {
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null");
        setQuantity(quantity);
        this.stockQuantity = stockQuantity;
        this.pricePerUnit = pricePerUnit;
    }

    public void setQuantity(int quantity) {
        if (quantity <= 0) throw Exceptions.invalidOrder("Quantity must be greater than zero");
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }

    public boolean isInStock() {
        return quantity <= stockQuantity;
    }
}
