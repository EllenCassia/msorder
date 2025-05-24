package com.desafio.tecnico.klok.msorder.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "clients")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {
    
    @Id
    @UuidGenerator
    @Column(name = "id", length = 36)
    @EqualsAndHashCode.Include
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;
    
    @Column(name = "vip", nullable = false)
    private Boolean vip = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    public Client(String name, String email, Boolean vip) {
        this.name = name;
        this.email = email;
        this.vip = vip;
    }

    public Client() {
    }

}