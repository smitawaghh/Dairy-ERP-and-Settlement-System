package com.smita.dairy.settlement;

import com.smita.dairy.settlement.dto.SettlementRequest;
import com.smita.dairy.settlement.dto.SettlementResponse;

import java.util.List;

public interface SettlementService {

    SettlementResponse createSettlement(
            SettlementRequest request
    );

    SettlementResponse getSettlementById(
            Long id
    );

    List<SettlementResponse> getAllSettlements();

    List<SettlementResponse> getSettlementsByFarmer(
            Long farmerId
    );
}