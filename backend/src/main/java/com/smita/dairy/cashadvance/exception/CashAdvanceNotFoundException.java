package com.smita.dairy.cashadvance.exception;

public class CashAdvanceNotFoundException
        extends RuntimeException {

    public CashAdvanceNotFoundException(Long id) {
        super("Cash advance not found with id: " + id);
    }
}