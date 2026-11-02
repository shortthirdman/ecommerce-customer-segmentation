package com.shortthirdman.primekit.customersegmentation.web;

import com.shortthirdman.primekit.customersegmentation.application.batch.CustomerSegmentationService;
import com.shortthirdman.primekit.customersegmentation.application.service.CustomerDataService;
import com.shortthirdman.primekit.customersegmentation.domain.model.CustomerStatistics;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Objects;

@ApplicationScoped
public class DashboardService {

    @Inject
    private CustomerDataService customerDataService;

    @Inject
    private CustomerSegmentationService segmentationService;

    public DashboardService() {
    }

    DashboardService(
            CustomerDataService customerDataService,
            CustomerSegmentationService segmentationService) {
        this.customerDataService = Objects.requireNonNull(customerDataService);
        this.segmentationService = Objects.requireNonNull(segmentationService);
    }

    public DashboardState initializeState() {
        customerDataService.initializeIfEmpty();
        return currentState();
    }

    public DashboardState currentState() {
        CustomerStatistics statistics =
                customerDataService.statistics();

        return new DashboardState(
                statistics.tierCounts(),
                statistics.totalCustomers(),
                segmentationService.latestStatus()
                        .map(Enum::name)
                        .orElse("NOT_STARTED"),
                segmentationService.isRunning());
    }
}
