package com.smita.dairy.ledger.mapper;

import com.smita.dairy.ledger.LedgerEntry;
import com.smita.dairy.ledger.dto.LedgerEntryResponse;

import org.springframework.stereotype.Component;

@Component
public class LedgerMapper {

    public LedgerEntryResponse toResponse(
            LedgerEntry entry) {

        LedgerEntryResponse response =
                new LedgerEntryResponse();

        response.setId(entry.getId());

        response.setFarmerId(
                entry.getFarmer().getId()
        );

        response.setFarmerCode(
                entry.getFarmer().getFarmerCode()
        );

        response.setFarmerName(
                entry.getFarmer().getFullName()
        );

        response.setTransactionDate(
                entry.getTransactionDate()
        );

        response.setEntryType(
                entry.getEntryType()
        );

        response.setTransactionType(
                entry.getTransactionType()
        );

        response.setReferenceId(
                entry.getReferenceId()
        );

        response.setAmount(
                entry.getAmount()
        );

        response.setDescription(
                entry.getDescription()
        );

        response.setCreatedAt(
                entry.getCreatedAt()
        );

        return response;
    }
}