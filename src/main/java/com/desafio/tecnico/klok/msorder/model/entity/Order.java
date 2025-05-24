package com.desafio.tecnico.klok.msorder.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "total_amount_with_discount", precision = 10, scale = 2)
    private BigDecimal totalAmountWithDiscount;

    @Column(name = "delivery_date")
    private Instant deliveryDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "in_stock", nullable = false)
    private boolean inStock = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public Order() {}

    public Order(Client client, List<OrderItem> items) {
        this.client = client;
        if (items != null) {
            items.forEach(this::addItem);
        }
        this.status = OrderStatus.PENDING;
        this.totalAmount = BigDecimal.ZERO;
        this.inStock = true;
    }

    public UUID getClientId() {
        return client != null ? client.getId() : null;
    }

    public void addItem(OrderItem item) {
        if (item != null) {
            this.items.add(item);
            item.setOrder(this);
        }
    }
    
}
