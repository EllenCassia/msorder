package com.desafio.tecnico.klok.msorder.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.desafio.tecnico.klok.msorder.business.service.ProductService;
import com.desafio.tecnico.klok.msorder.model.dto.product.ProductRequestDTO;
import com.desafio.tecnico.klok.msorder.model.dto.stock.StockUpdateDTO;
import com.desafio.tecnico.klok.msorder.model.entity.Product;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*") 
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Erro ao buscar produto {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllActiveProducts() {
        try {
            List<Product> products = productService.getAllActiveProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Erro ao buscar produtos: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Product>> getAvailableProducts() {
        try {
            List<Product> products = productService.getAvailableProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Erro ao buscar produtos disponíveis: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody ProductRequestDTO request) {
        try {
            Product product = productService.createProduct(
                    request.name(),
                    request.price(),
                    request.stockQuantity()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(product);
        } catch (Exception e) {
            log.error("Erro ao criar produto: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateProductStock(@PathVariable UUID id, @Valid @RequestBody StockUpdateDTO request) {
        try {
            Product product = productService.updateProductStock(id, request.stockQuantity());
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            log.error("Erro ao atualizar estoque do produto {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getProductsWithLowStock(@RequestParam(defaultValue = "10") int threshold) {
        try {
            List<Product> products = productService.getProductsWithLowStock(threshold);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            log.error("Erro ao buscar produtos com estoque baixo: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/{id}/name")
    public ResponseEntity<String> getProductNameById(@PathVariable UUID id) {
        try {
            String name = productService.findNameById(id);
            return ResponseEntity.ok(name);
        } catch (Exception e) {
            log.error("Erro ao buscar nome do produto {}: {}", id, e.getMessage());
            throw e;
        }
    }
}