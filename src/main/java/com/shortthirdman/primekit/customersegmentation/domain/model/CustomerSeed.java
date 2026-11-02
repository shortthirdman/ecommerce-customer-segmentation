package com.shortthirdman.primekit.customersegmentation.domain.model;

import com.shortthirdman.primekit.customersegmentation.domain.entity.Customer;
import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;

import java.math.BigDecimal;

public record CustomerSeed(
        String id,
        String name,
        BigDecimal totalSpent,
        CustomerTier tier) {

    public Customer toCustomer() {
        return Customer.builder()
                .id(id)
                .name(name)
                .totalSpent(totalSpent)
                .tier(tier)
                .build();
    }
}
