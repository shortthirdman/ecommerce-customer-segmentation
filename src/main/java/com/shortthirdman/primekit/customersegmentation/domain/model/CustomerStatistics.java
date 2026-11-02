package com.shortthirdman.primekit.customersegmentation.domain.model;

import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;

import java.util.Map;

public record CustomerStatistics(
        long totalCustomers,
        Map<CustomerTier, Long> tierCounts) {

    public CustomerStatistics {
        tierCounts = Map.copyOf(tierCounts);
    }
}