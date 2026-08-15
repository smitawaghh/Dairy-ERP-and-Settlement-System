package com.smita.dairy.dashboard;

import com.smita.dairy.dashboard.dto.DashboardSummaryResponse;

import java.time.LocalDate;

public interface DashboardService {

    DashboardSummaryResponse getSummary(
            LocalDate date
    );
}