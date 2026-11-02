package com.shortthirdman.primekit.customersegmentation.web;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

@Named
@ViewScoped
public class DashboardBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Inject
    private DashboardService dashboardService;

    @Getter
    private DashboardState state;

    @PostConstruct
    public void initialize() {
        state = dashboardService.initializeState();
    }

    public void refresh() {
        state = dashboardService.currentState();
    }

}
