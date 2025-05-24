package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import org.springframework.stereotype.Service;

import com.desafio.tecnico.klok.msorder.business.service.StockValidatorService;
import com.desafio.tecnico.klok.msorder.exception.Exceptions;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import com.desafio.tecnico.klok.msorder.model.entity.Product;
import com.desafio.tecnico.klok.msorder.model.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class StockValidatorServiceImpl implements StockValidatorService {

    private final ProductRepository productRepository;

    public StockValidatorServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public boolean validate(Order order) {

        return order.getItems().stream()
                .allMatch(item -> {
                    Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> Exceptions.productNotFound(item.getProductId()));
                    return item.getQuantity() <= product.getStockQuantity();
                });
    }

    @Override
    @Transactional
    public void reserveStock(Order order) {
       
        if (!validate(order)) {
            throw Exceptions.insufficientStock(order.getId(), 1, 0);
        }
        
        order.getItems().forEach(item -> {
            Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> Exceptions.productNotFound(item.getProductId()));
            
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        });
    }

}
