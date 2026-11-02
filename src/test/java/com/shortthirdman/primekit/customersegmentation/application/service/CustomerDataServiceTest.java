package com.shortthirdman.primekit.customersegmentation.application.service;

import com.shortthirdman.primekit.customersegmentation.application.batch.CustomerSegmentationPolicy;
import com.shortthirdman.primekit.customersegmentation.domain.entity.Customer;
import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;
import com.shortthirdman.primekit.customersegmentation.domain.model.CustomerStatistics;
import com.shortthirdman.primekit.customersegmentation.domain.model.SegmentationPreview;
import com.shortthirdman.primekit.customersegmentation.domain.model.SegmentationThreshold;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer data service")
class CustomerDataServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Given seed JSON, when customers are loaded, then records are converted to entities")
    void shouldConvertSeedRecordsToCustomers() {
        List<Customer> customers = new CustomerDataService().loadCustomers();

        assertAll(
                () -> assertEquals(100, customers.size()),
                () -> assertNotNull(customers.getFirst().getId()),
                () -> assertNotNull(customers.getFirst().getTier())
        );
    }

    @Test
    @DisplayName("Given an unclassified customer, when statistics are calculated, then it is included without failing")
    void shouldIncludeUnclassifiedCustomerInTotal() {
        List<Customer> customers = List.of(
                customer("CUST-001", CustomerTier.BRONZE),
                customer("CUST-002", null)
        );

        CustomerStatistics statistics = CustomerDataService.summarize(customers);

        assertAll(
                () -> assertEquals(2, statistics.totalCustomers()),
                () -> assertEquals(1, statistics.tierCounts().get(CustomerTier.BRONZE))
        );
    }

    @Test
    @DisplayName("Given new thresholds, when previewing, then current and projected distributions are calculated")
    void shouldCalculateCurrentAndProjectedDistributions() {
        List<Customer> customers = List.of(
                customer("CUST-001", "7500", CustomerTier.SILVER),
                customer("CUST-002", "15000", CustomerTier.PLATINUM)
        );
        CustomerSegmentationPolicy policy = policy();

        SegmentationPreview preview = CustomerDataService.calculatePreview(customers, policy);

        assertAll(
                () -> assertEquals(1, preview.current().tierCounts().get(CustomerTier.SILVER)),
                () -> assertEquals(1, preview.projected().tierCounts().get(CustomerTier.GOLD)),
                () -> assertEquals(2, preview.current().totalCustomers()),
                () -> assertEquals(2, preview.projected().totalCustomers())
        );
    }

    @Test
    @DisplayName("Given preview calculation, when tiers are projected, then persisted customers are not modified")
    void shouldNotModifyCustomersDuringPreview() {
        Customer customer = customer("CUST-001", "7500", CustomerTier.SILVER);

        CustomerDataService.calculatePreview(List.of(customer), policy());

        assertEquals(CustomerTier.SILVER, customer.getTier());
    }

    private static Customer customer(String id, CustomerTier tier) {
        return customer(id, "10", tier);
    }

    private static Customer customer(
            String id,
            String totalSpent,
            CustomerTier tier) {

        return Customer.builder()
                .id(id)
                .name("Customer")
                .totalSpent(new BigDecimal(totalSpent))
                .tier(tier)
                .build();
    }

    private static CustomerSegmentationPolicy policy() {
        return new CustomerSegmentationPolicy(List.of(
                threshold("10", CustomerTier.BRONZE),
                threshold("1000", CustomerTier.SILVER),
                threshold("5000", CustomerTier.GOLD),
                threshold("10000", CustomerTier.PLATINUM)
        ));
    }

    private static SegmentationThreshold threshold(String value, CustomerTier tier) {
        return new SegmentationThreshold(new BigDecimal(value), tier);
    }
}