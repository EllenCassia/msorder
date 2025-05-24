package com.desafio.tecnico.klok.msorder.business.service.impl.order;

import com.desafio.tecnico.klok.msorder.business.service.OrderCalculationService;
import com.desafio.tecnico.klok.msorder.business.service.ProductService;
import com.desafio.tecnico.klok.msorder.exception.Exceptions;
import com.desafio.tecnico.klok.msorder.model.entity.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderCalculationServiceImpl implements OrderCalculationService {

    private final ProductService productService;

    public OrderCalculationServiceImpl(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void calculateTotal(Order order) {
        BigDecimal total = order.getItems().stream()
            .map(item -> {
                if (item.getPricePerUnit() == null) {
                    var product = productService.getProductById(item.getProductId());
                    if (product == null) {
                        throw Exceptions.productNotFound(item.getProductId());
                    }
                    item.setPricePerUnit(product.getPrice());
                }
                return item.getTotalPrice();
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(total);
    }
}

