package com.smita.dairy.statement;

import com.smita.dairy.statement.dto.StatementResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statements")
public class StatementController {

    private final StatementService statementService;

    public StatementController(
            StatementService statementService) {

        this.statementService = statementService;
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<StatementResponse>
    generateStatement(
            @PathVariable Long farmerId,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate from,

            @RequestParam
            @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE
            )
            LocalDate to) {

        StatementResponse response =
                statementService.generateStatement(
                        farmerId,
                        from,
                        to
                );

        return ResponseEntity.ok(response);
    }
}