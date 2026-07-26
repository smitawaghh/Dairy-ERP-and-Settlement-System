package com.smita.dairy.ledger;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.ledger.dto.LedgerEntryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface LedgerService {

    LedgerEntry createEntry(
            Farmer farmer,
            LocalDate transactionDate,
            LedgerEntryType entryType,
            LedgerTransactionType transactionType,
            Long referenceId,
            BigDecimal amount,
            String description
    );

    LedgerEntryResponse getLedgerEntryById(Long id);

    List<LedgerEntryResponse> getFarmerLedger(Long farmerId);

    List<LedgerEntryResponse> getFarmerLedgerByDateRange(
            Long farmerId,
            LocalDate from,
            LocalDate to
    );

    BigDecimal getFarmerBalance(Long farmerId);
}