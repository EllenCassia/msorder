package com.desafio.tecnico.klok.msorder.model.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Product {
    
    public Product(String name2, BigDecimal price2, int stockQuantity2) {
        this.name = name2;
        this.price = price2;
        this.stockQuantity = stockQuantity2;
    }

    public Product() {
    }

    @Id
    @UuidGenerator
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;
    
    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Version
    @Column(name = "version")
    private Long version;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}