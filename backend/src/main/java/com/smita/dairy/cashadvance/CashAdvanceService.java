package com.smita.dairy.cashadvance;

import com.smita.dairy.cashadvance.dto.CashAdvanceRequest;
import com.smita.dairy.cashadvance.dto.CashAdvanceResponse;

import java.time.LocalDate;
import java.util.List;

public interface CashAdvanceService {

    CashAdvanceResponse createCashAdvance(
            CashAdvanceRequest request
    );

    CashAdvanceResponse getCashAdvanceById(
            Long id
    );

    List<CashAdvanceResponse> getAllCashAdvances();

    List<CashAdvanceResponse> getCashAdvancesByFarmer(
            Long farmerId
    );

    List<CashAdvanceResponse> getCashAdvancesByDateRange(
            LocalDate from,
            LocalDate to
    );
}