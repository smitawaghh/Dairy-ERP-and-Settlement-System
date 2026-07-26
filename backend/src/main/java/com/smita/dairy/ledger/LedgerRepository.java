package com.smita.dairy.ledger;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LedgerRepository
        extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry>
    findByFarmerIdOrderByTransactionDateDescIdDesc(Long farmerId);

    List<LedgerEntry>
    findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
            Long farmerId,
            LocalDate from,
            LocalDate to
    );

    Optional<LedgerEntry>
    findByTransactionTypeAndReferenceId(
            LedgerTransactionType transactionType,
            Long referenceId
    );

    boolean existsByTransactionTypeAndReferenceId(
            LedgerTransactionType transactionType,
            Long referenceId
    );
}