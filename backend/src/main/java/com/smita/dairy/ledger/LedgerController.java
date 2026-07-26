package com.smita.dairy.ledger;

import com.smita.dairy.ledger.dto.LedgerEntryResponse;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(
            LedgerService ledgerService) {

        this.ledgerService = ledgerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LedgerEntryResponse>
    getLedgerEntryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ledgerService.getLedgerEntryById(id)
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<LedgerEntryResponse>>
    getFarmerLedger(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                ledgerService.getFarmerLedger(farmerId)
        );
    }

    @GetMapping("/farmer/{farmerId}/range")
    public ResponseEntity<List<LedgerEntryResponse>>
    getFarmerLedgerByDateRange(
            @PathVariable Long farmerId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

        return ResponseEntity.ok(
                ledgerService.getFarmerLedgerByDateRange(
                        farmerId,
                        from,
                        to
                )
        );
    }

    @GetMapping("/farmer/{farmerId}/balance")
    public ResponseEntity<BigDecimal>
    getFarmerBalance(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                ledgerService.getFarmerBalance(farmerId)
        );
    }
}