package com.smita.dairy.settlement;

import com.smita.dairy.settlement.dto.SettlementRequest;
import com.smita.dairy.settlement.dto.SettlementResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(
            SettlementService settlementService) {

        this.settlementService =
                settlementService;
    }

    @PostMapping
    public ResponseEntity<SettlementResponse>
    createSettlement(
            @Valid
            @RequestBody
            SettlementRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        settlementService
                                .createSettlement(request)
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SettlementResponse>
    getSettlementById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                settlementService
                        .getSettlementById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<SettlementResponse>>
    getAllSettlements() {

        return ResponseEntity.ok(
                settlementService
                        .getAllSettlements()
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<SettlementResponse>>
    getSettlementsByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                settlementService
                        .getSettlementsByFarmer(
                                farmerId
                        )
        );
    }
}