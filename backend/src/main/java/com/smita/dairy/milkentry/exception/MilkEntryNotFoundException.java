package com.smita.dairy.milkentry.exception;

public class MilkEntryNotFoundException extends RuntimeException {

    public MilkEntryNotFoundException(Long id) {
        super("Milk entry not found with id: " + id);
    }
}