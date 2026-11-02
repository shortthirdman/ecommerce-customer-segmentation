package com.shortthirdman.primekit.customersegmentation.web;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class CustomerSegmentationFlowState implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private List<ThresholdInput> thresholds = List.of();
    private List<TierComparison> comparisons = List.of();
    private long currentTotal;
    private long projectedTotal;

    void setThresholds(List<ThresholdInput> thresholds) {
        this.thresholds = List.copyOf(thresholds);
    }

    void setComparison(
            List<TierComparison> comparisons,
            long currentTotal,
            long projectedTotal) {
        this.comparisons = List.copyOf(comparisons);
        this.currentTotal = currentTotal;
        this.projectedTotal = projectedTotal;
    }
}
