package com.smita.dairy.statement;

import com.smita.dairy.statement.dto.StatementResponse;

import java.time.LocalDate;

public interface StatementService {

    StatementResponse generateStatement(
            Long farmerId,
            LocalDate from,
            LocalDate to
    );
}