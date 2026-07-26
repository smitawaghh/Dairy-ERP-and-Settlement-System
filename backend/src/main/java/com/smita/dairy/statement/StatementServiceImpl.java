package com.smita.dairy.statement;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.ledger.LedgerEntry;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerRepository;
import com.smita.dairy.ledger.dto.LedgerEntryResponse;
import com.smita.dairy.ledger.mapper.LedgerMapper;

import com.smita.dairy.statement.dto.StatementResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StatementServiceImpl
        implements StatementService {

    private final FarmerRepository farmerRepository;
    private final LedgerRepository ledgerRepository;
    private final LedgerMapper ledgerMapper;

    public StatementServiceImpl(
            FarmerRepository farmerRepository,
            LedgerRepository ledgerRepository,
            LedgerMapper ledgerMapper) {

        this.farmerRepository = farmerRepository;
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
    }

    @Override
    public StatementResponse generateStatement(
            Long farmerId,
            LocalDate from,
            LocalDate to) {

        if (farmerId == null) {
            throw new IllegalArgumentException(
                    "Farmer id is required"
            );
        }

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "Statement from and to dates are required"
            );
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        Farmer farmer =
                farmerRepository
                        .findById(farmerId)
                        .orElseThrow(() ->
                                new FarmerNotFoundException(
                                        farmerId
                                )
                        );

        List<LedgerEntry> entries =
                ledgerRepository
                        .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                                farmerId,
                                from,
                                to
                        );

        BigDecimal totalCredits =
                BigDecimal.ZERO;

        BigDecimal totalDebits =
                BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {

            if (entry.getEntryType()
                    == LedgerEntryType.CREDIT) {

                totalCredits =
                        totalCredits.add(
                                entry.getAmount()
                        );

            } else if (entry.getEntryType()
                    == LedgerEntryType.DEBIT) {

                totalDebits =
                        totalDebits.add(
                                entry.getAmount()
                        );
            }
        }

        totalCredits =
                totalCredits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        totalDebits =
                totalDebits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal netAmount =
                totalCredits
                        .subtract(totalDebits)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        List<LedgerEntryResponse> transactions =
                entries
                        .stream()
                        .map(ledgerMapper::toResponse)
                        .toList();

        StatementResponse response =
                new StatementResponse();

        response.setFarmerId(
                farmer.getId()
        );

        response.setFarmerCode(
                farmer.getFarmerCode()
        );

        response.setFarmerName(
                farmer.getFullName()
        );

        response.setPeriodFrom(from);
        response.setPeriodTo(to);

        response.setTotalCredits(
                totalCredits
        );

        response.setTotalDebits(
                totalDebits
        );

        response.setNetAmount(
                netAmount
        );

        response.setTransactions(
                transactions
        );

        return response;
    }
}