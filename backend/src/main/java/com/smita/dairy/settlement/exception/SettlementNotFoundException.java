package com.smita.dairy.settlement.exception;

public class SettlementNotFoundException
        extends RuntimeException {

    public SettlementNotFoundException(Long id) {
        super("Settlement not found with id: " + id);
    }
}