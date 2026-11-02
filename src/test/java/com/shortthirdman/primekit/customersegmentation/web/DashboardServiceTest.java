package com.shortthirdman.primekit.customersegmentation.web;

import com.shortthirdman.primekit.customersegmentation.application.batch.CustomerSegmentationService;
import com.shortthirdman.primekit.customersegmentation.application.service.CustomerDataService;
import com.shortthirdman.primekit.customersegmentation.domain.entity.CustomerTier;
import com.shortthirdman.primekit.customersegmentation.domain.model.CustomerStatistics;
import jakarta.batch.runtime.BatchStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Dashboard Service")
class DashboardServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("When initialized, then seed data and dashboard state are loaded")
    void shouldInitializeDashboardState() {
        StubCustomerDataService customerDataService = new StubCustomerDataService();
        DashboardService service = new DashboardService(
                customerDataService,
                new StubSegmentationService());

        DashboardState state = service.initializeState();

        assertAll(
                () -> assertTrue(customerDataService.initialized),
                () -> assertEquals(10, state.getTotalCustomers()),
                () -> assertEquals(4, state.getGoldCount()),
                () -> assertEquals(40.0, state.getGoldPercentage()),
                () -> assertEquals("STARTED", state.getLatestBatchStatus()),
                () -> assertTrue(state.isSegmentationRunning())
        );
    }

    private static class StubCustomerDataService extends CustomerDataService {

        private boolean initialized;

        @Override
        public void initializeIfEmpty() {
            initialized = true;
        }

        @Override
        public CustomerStatistics statistics() {
            EnumMap<CustomerTier, Long> counts = new EnumMap<>(CustomerTier.class);
            counts.put(CustomerTier.BRONZE, 2L);
            counts.put(CustomerTier.SILVER, 3L);
            counts.put(CustomerTier.GOLD, 4L);
            counts.put(CustomerTier.PLATINUM, 1L);
            return new CustomerStatistics(10, counts);
        }
    }

    private static class StubSegmentationService extends CustomerSegmentationService {

        @Override
        public Optional<BatchStatus> latestStatus() {
            return Optional.of(BatchStatus.STARTED);
        }

        @Override
        public boolean isRunning() {
            return true;
        }
    }
}