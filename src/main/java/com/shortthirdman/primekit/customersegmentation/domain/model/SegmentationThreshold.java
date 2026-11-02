package com.shortthirdman.primekit.customersegmentation.domain.model;

import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;

import java.math.BigDecimal;

public record SegmentationThreshold(BigDecimal minimumValue, CustomerTier tier) {
}
