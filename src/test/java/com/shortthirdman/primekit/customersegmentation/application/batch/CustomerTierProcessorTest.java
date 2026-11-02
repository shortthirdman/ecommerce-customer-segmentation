package com.shortthirdman.primekit.customersegmentation.application.batch;

import com.shortthirdman.primekit.customersegmentation.domain.entity.Customer;
import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;
import com.shortthirdman.primekit.customersegmentation.domain.model.SegmentationThreshold;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Tier Processor")
class CustomerTierProcessorTest {

    private CustomerTierProcessor processor;

    @BeforeEach
    void setUp() {
        processor = new CustomerTierProcessor(new CustomerSegmentationPolicy(List.of(
                threshold("10", CustomerTier.BRONZE),
                threshold("1000", CustomerTier.SILVER),
                threshold("5000", CustomerTier.GOLD),
                threshold("10000", CustomerTier.PLATINUM)
        )));
    }

    @AfterEach
    void tearDown() {
    }

    @Nested
    @DisplayName("Given a customer already in the calculated tier")
    class MatchingTier {

        @Test
        @DisplayName("When processed, then the customer is filtered")
        void shouldFilterCustomer() {
            Customer customer = customer("7500", CustomerTier.GOLD);

            assertNull(processor.processItem(customer));
        }
    }

    @Nested
    @DisplayName("Given a customer with an outdated tier")
    class OutdatedTier {

        @Test
        @DisplayName("When processed, then an updated customer is returned")
        void shouldReturnCustomerForUpdate() {
            Customer customer = customer("7500", CustomerTier.SILVER);

            Customer updated = processor.processItem(customer);

            assertEquals(CustomerTier.GOLD, updated.getTier());
        }
    }

    private static Customer customer(String totalSpent, CustomerTier tier) {
        return Customer.builder()
                .id("CUST-001")
                .name("Customer")
                .totalSpent(new BigDecimal(totalSpent))
                .tier(tier)
                .build();
    }

    private static SegmentationThreshold threshold(String value, CustomerTier tier) {
        return new SegmentationThreshold(new BigDecimal(value), tier);
    }
}