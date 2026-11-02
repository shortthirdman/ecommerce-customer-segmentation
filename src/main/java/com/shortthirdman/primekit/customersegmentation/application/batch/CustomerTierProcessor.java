package com.shortthirdman.primekit.customersegmentation.application.batch;

import com.shortthirdman.primekit.customersegmentation.domain.entity.Customer;
import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;
import jakarta.annotation.PostConstruct;
import jakarta.batch.api.BatchProperty;
import jakarta.batch.api.chunk.ItemProcessor;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.Objects;

@Dependent
@Named("customerTierProcessor")
public class CustomerTierProcessor implements ItemProcessor {

    @Inject
    @BatchProperty(name = CustomerSegmentationPolicy.JOB_PARAMETER)
    private String thresholdsJson;

    private CustomerSegmentationPolicy policy;

    public CustomerTierProcessor() {
    }

    CustomerTierProcessor(CustomerSegmentationPolicy policy) {
        this.policy = Objects.requireNonNull(policy);
    }

    @PostConstruct
    void initialize() {
        policy = CustomerSegmentationPolicy.fromJson(thresholdsJson);
    }

    @Override
    public Customer processItem(Object item) {
        if (!(item instanceof Customer customer)) {
            throw new IllegalArgumentException("Expected a Customer item");
        }
        CustomerTier calculatedTier = policy.tierFor(customer.getTotalSpent());
        if (calculatedTier == customer.getTier()) {
            return null;
        }
        return Customer.builder()
                .id(customer.getId())
                .name(customer.getName())
                .totalSpent(customer.getTotalSpent())
                .tier(calculatedTier)
                .build();
    }
}
