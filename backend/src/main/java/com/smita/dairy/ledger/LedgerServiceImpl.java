package com.smita.dairy.ledger;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;
import com.smita.dairy.ledger.dto.LedgerEntryResponse;
import com.smita.dairy.ledger.exception.LedgerEntryNotFoundException;
import com.smita.dairy.ledger.mapper.LedgerMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LedgerServiceImpl implements LedgerService {

    private final LedgerRepository ledgerRepository;
    private final FarmerRepository farmerRepository;
    private final LedgerMapper ledgerMapper;

    public LedgerServiceImpl(
            LedgerRepository ledgerRepository,
            FarmerRepository farmerRepository,
            LedgerMapper ledgerMapper) {

        this.ledgerRepository = ledgerRepository;
        this.farmerRepository = farmerRepository;
        this.ledgerMapper = ledgerMapper;
    }

    @Override
    public LedgerEntry createEntry(
            Farmer farmer,
            LocalDate transactionDate,
            LedgerEntryType entryType,
            LedgerTransactionType transactionType,
            Long referenceId,
            BigDecimal amount,
            String description) {

        if (farmer == null) {
            throw new IllegalArgumentException(
                    "Farmer is required for ledger entry"
            );
        }

        if (transactionDate == null) {
            throw new IllegalArgumentException(
                    "Transaction date is required"
            );
        }

        if (entryType == null) {
            throw new IllegalArgumentException(
                    "Ledger entry type is required"
            );
        }

        if (transactionType == null) {
            throw new IllegalArgumentException(
                    "Ledger transaction type is required"
            );
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Ledger amount must be greater than zero"
            );
        }

        if (referenceId != null
                && ledgerRepository
                .existsByTransactionTypeAndReferenceId(
                        transactionType,
                        referenceId
                )) {

            throw new IllegalStateException(
                    "Ledger entry already exists for "
                            + transactionType
                            + " reference id "
                            + referenceId
            );
        }

        LedgerEntry entry = new LedgerEntry();

        entry.setFarmer(farmer);
        entry.setTransactionDate(transactionDate);
        entry.setEntryType(entryType);
        entry.setTransactionType(transactionType);
        entry.setReferenceId(referenceId);
        entry.setAmount(amount);
        entry.setDescription(description);

        return ledgerRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public LedgerEntryResponse getLedgerEntryById(Long id) {

        LedgerEntry entry = ledgerRepository
                .findById(id)
                .orElseThrow(() ->
                        new LedgerEntryNotFoundException(id)
                );

        return ledgerMapper.toResponse(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> getFarmerLedger(
            Long farmerId) {

        validateFarmer(farmerId);

        return ledgerRepository
                .findByFarmerIdOrderByTransactionDateDescIdDesc(
                        farmerId
                )
                .stream()
                .map(ledgerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<LedgerEntryResponse> getFarmerLedgerByDateRange(
            Long farmerId,
            LocalDate from,
            LocalDate to) {

        validateFarmer(farmerId);

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "From and to dates are required"
            );
        }

        if (from.isAfter(to)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
            );
        }

        return ledgerRepository
                .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                        farmerId,
                        from,
                        to
                )
                .stream()
                .map(ledgerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getFarmerBalance(Long farmerId) {

        validateFarmer(farmerId);

        List<LedgerEntry> entries =
                ledgerRepository
                        .findByFarmerIdOrderByTransactionDateDescIdDesc(
                                farmerId
                        );

        BigDecimal balance = BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {

            if (entry.getEntryType()
                    == LedgerEntryType.CREDIT) {

                balance = balance.add(
                        entry.getAmount()
                );

            } else {

                balance = balance.subtract(
                        entry.getAmount()
                );
            }
        }

        return balance;
    }

    private void validateFarmer(Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(farmerId);
        }
    }
}