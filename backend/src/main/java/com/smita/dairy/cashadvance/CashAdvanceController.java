package com.smita.dairy.cashadvance;

import com.smita.dairy.cashadvance.dto.CashAdvanceRequest;
import com.smita.dairy.cashadvance.dto.CashAdvanceResponse;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cash-advances")
public class CashAdvanceController {

    private final CashAdvanceService cashAdvanceService;

    public CashAdvanceController(
            CashAdvanceService cashAdvanceService) {

        this.cashAdvanceService =
                cashAdvanceService;
    }

    @PostMapping
    public ResponseEntity<CashAdvanceResponse>
    createCashAdvance(
            @Valid @RequestBody CashAdvanceRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        cashAdvanceService
                                .createCashAdvance(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CashAdvanceResponse>
    getCashAdvanceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cashAdvanceService
                        .getCashAdvanceById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<CashAdvanceResponse>>
    getAllCashAdvances() {

        return ResponseEntity.ok(
                cashAdvanceService
                        .getAllCashAdvances()
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<CashAdvanceResponse>>
    getCashAdvancesByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                cashAdvanceService
                        .getCashAdvancesByFarmer(farmerId)
        );
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<CashAdvanceResponse>>
    getCashAdvancesByDateRange(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        return ResponseEntity.ok(
                cashAdvanceService
                        .getCashAdvancesByDateRange(from, to)
        );
    }
}