package com.shortthirdman.primekit.customersegmentation.web;

import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;
import com.shortthirdman.primekit.customersegmentation.domain.model.SegmentationThreshold;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class ThresholdInput implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private CustomerTier tier;
    @Setter
    private BigDecimal minimumValue;

    public ThresholdInput() {
    }

    ThresholdInput(SegmentationThreshold threshold) {
        tier = threshold.tier();
        minimumValue = threshold.minimumValue();
    }

    SegmentationThreshold toThreshold() {
        return new SegmentationThreshold(minimumValue, tier);
    }
}
