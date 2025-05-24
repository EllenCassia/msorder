package com.desafio.tecnico.klok.msorder.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.desafio.tecnico.klok.msorder.model.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    
    Optional<Client> findByEmail(String email);
    
    @Query("SELECT c FROM Client c WHERE c.vip = true")
    List<Client> findAllVipClients();
    
    @Query("SELECT COUNT(c) FROM Client c WHERE c.vip = true")
    long countVipClients();
    
    boolean existsByEmail(String email);
}

