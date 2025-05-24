package com.desafio.tecnico.klok.msorder.business.service.impl.factory;

import org.springframework.stereotype.Service;

import com.desafio.tecnico.klok.msorder.business.service.strategy.DiscountStrategy;
import com.desafio.tecnico.klok.msorder.business.service.strategy.VipDiscountStrategy;
import com.desafio.tecnico.klok.msorder.model.dto.client.ClientResponseDTO;

@Service
public class DiscountStrategyFactory {

    private final VipDiscountStrategy vipDiscountStrategy;

    public DiscountStrategyFactory(VipDiscountStrategy vipDiscountStrategy) {
        this.vipDiscountStrategy = vipDiscountStrategy;
    }

    public DiscountStrategy getStrategy(ClientResponseDTO client) {
     
        if (client.vip()) {
            return vipDiscountStrategy;
        }
        return order -> order.getTotalAmount();
    }
}

