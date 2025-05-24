package com.desafio.tecnico.klok.msorder.business.service;


import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.desafio.tecnico.klok.msorder.model.entity.Product;

public interface ProductService {

    Product getProductById(UUID productId);

    List<Product> getAllActiveProducts();

    List<Product> getAvailableProducts();

    Product createProduct(String name, BigDecimal price, int stockQuantity);

    Product updateProductStock(UUID productId, int newStock);

    List<Product> getProductsWithLowStock(int threshold);

    String findNameById(UUID productId);
}

