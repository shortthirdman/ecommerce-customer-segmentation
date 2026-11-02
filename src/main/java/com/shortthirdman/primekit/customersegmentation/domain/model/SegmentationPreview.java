package com.shortthirdman.primekit.customersegmentation.domain.model;

public record SegmentationPreview(
        CustomerStatistics current,
        CustomerStatistics projected) {

    public SegmentationPreview {
        if (current.totalCustomers() != projected.totalCustomers()) {
            throw new IllegalStateException(
                    "Current and projected customer totals must match");
        }
    }
}
