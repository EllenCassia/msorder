package com.desafio.tecnico.klok.msorder.business.service.impl.product;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.desafio.tecnico.klok.msorder.business.service.ProductService;
import com.desafio.tecnico.klok.msorder.exception.BusinessException;
import com.desafio.tecnico.klok.msorder.exception.Exceptions;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import com.desafio.tecnico.klok.msorder.model.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product getProductById(UUID productId) {
        log.debug("Buscando produto por ID: {}", productId);
        return productRepository.findByIdAndActiveTrue(productId)
                .orElseThrow(() -> Exceptions.productNotFound(productId));
    }

    @Override
    public List<Product> getAllActiveProducts() {
        log.debug("Buscando todos os produtos ativos");
        return productRepository.findByActiveTrue();
    }

    @Override
    public List<Product> getAvailableProducts() {
        log.debug("Buscando produtos disponíveis em estoque");
        return productRepository.findAvailableProducts();
    }

    @Override
    @Transactional
    public Product createProduct(String name, BigDecimal price, int stockQuantity) {
        log.debug("Criando novo produto: {}", name);
        Product product = new Product(name, price, stockQuantity);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product updateProductStock(UUID productId, int newStock) {
        log.debug("Atualizando estoque do produto {} para {}", productId, newStock);
        Product product = getProductById(productId);
        product.setStockQuantity(newStock);
        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsWithLowStock(int threshold) {
        log.debug("Buscando produtos com estoque baixo (threshold: {})", threshold);
        return productRepository.findProductsWithLowStock(threshold);
    }

    public String findNameById(UUID productId) {
        return productRepository.findById(productId)
            .map(Product::getName)
            .orElseThrow(() -> new BusinessException("Produto não encontrado: " + productId, "PRODUCT_NOT_FOUND"));
    }

}
