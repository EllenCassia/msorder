package com.desafio.tecnico.klok.msorder.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.desafio.tecnico.klok.msorder.model.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    
    List<Product> findByActiveTrue();
    
    Optional<Product> findByIdAndActiveTrue(UUID id);
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity > 0 AND p.active = true")
    List<Product> findAvailableProducts();
    
    @Query("SELECT p FROM Product p WHERE p.stockQuantity <= :threshold AND p.active = true")
    List<Product> findProductsWithLowStock(@Param("threshold") int threshold);
    
    @Query("SELECT p FROM Product p WHERE UPPER(p.name) LIKE UPPER(CONCAT('%', :name, '%')) AND p.active = true")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);
    
    boolean existsByNameAndActiveTrue(String name);

    @Query("SELECT p.name FROM Product p WHERE p.id = :id")
    String findNameById(@Param("id") UUID id);
}