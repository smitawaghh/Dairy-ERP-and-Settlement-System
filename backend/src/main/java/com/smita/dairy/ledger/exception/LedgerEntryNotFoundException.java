package com.smita.dairy.ledger.exception;

public class LedgerEntryNotFoundException
        extends RuntimeException {

    public LedgerEntryNotFoundException(Long id) {
        super("Ledger entry not found with id: " + id);
    }
}