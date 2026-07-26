package com.smita.dairy.cashadvance.mapper;

import com.smita.dairy.cashadvance.CashAdvance;
import com.smita.dairy.cashadvance.dto.CashAdvanceRequest;
import com.smita.dairy.cashadvance.dto.CashAdvanceResponse;

import org.springframework.stereotype.Component;

@Component
public class CashAdvanceMapper {

    public CashAdvance toEntity(
            CashAdvanceRequest request) {

        CashAdvance cashAdvance = new CashAdvance();

        cashAdvance.setAdvanceDate(
                request.getAdvanceDate()
        );

        cashAdvance.setAmount(
                request.getAmount()
        );

        cashAdvance.setRemarks(
                request.getRemarks()
        );

        return cashAdvance;
    }

    public CashAdvanceResponse toResponse(
            CashAdvance cashAdvance) {

        CashAdvanceResponse response =
                new CashAdvanceResponse();

        response.setId(
                cashAdvance.getId()
        );

        response.setFarmerId(
                cashAdvance.getFarmer().getId()
        );

        response.setFarmerCode(
                cashAdvance.getFarmer().getFarmerCode()
        );

        response.setFarmerName(
                cashAdvance.getFarmer().getFullName()
        );

        response.setAdvanceDate(
                cashAdvance.getAdvanceDate()
        );

        response.setAmount(
                cashAdvance.getAmount()
        );

        response.setRemarks(
                cashAdvance.getRemarks()
        );

        response.setCreatedAt(
                cashAdvance.getCreatedAt()
        );

        return response;
    }
}