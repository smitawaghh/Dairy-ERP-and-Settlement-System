package com.smita.dairy.settlement.mapper;

import com.smita.dairy.settlement.Settlement;
import com.smita.dairy.settlement.dto.SettlementResponse;

import org.springframework.stereotype.Component;

@Component
public class SettlementMapper {

    public SettlementResponse toResponse(
            Settlement settlement) {

        SettlementResponse response =
                new SettlementResponse();

        response.setId(settlement.getId());

        response.setFarmerId(
                settlement.getFarmer().getId()
        );

        response.setFarmerCode(
                settlement.getFarmer().getFarmerCode()
        );

        response.setFarmerName(
                settlement.getFarmer().getFullName()
        );

        response.setPeriodFrom(
                settlement.getPeriodFrom()
        );

        response.setPeriodTo(
                settlement.getPeriodTo()
        );

        response.setTotalCredits(
                settlement.getTotalCredits()
        );

        response.setTotalDebits(
                settlement.getTotalDebits()
        );

        response.setNetAmount(
                settlement.getNetAmount()
        );

        response.setStatus(
                settlement.getStatus()
        );

        response.setRemarks(
                settlement.getRemarks()
        );

        response.setCreatedAt(
                settlement.getCreatedAt()
        );

        return response;
    }
}