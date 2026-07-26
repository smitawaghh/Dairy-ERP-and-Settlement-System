package com.smita.dairy.milkentry;

import com.smita.dairy.milkentry.dto.MilkEntryRequest;
import com.smita.dairy.milkentry.dto.MilkEntryResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/milk-entries")
public class MilkEntryController {

    private final MilkEntryService milkEntryService;

    public MilkEntryController(
            MilkEntryService milkEntryService) {

        this.milkEntryService = milkEntryService;
    }

    @PostMapping
    public ResponseEntity<MilkEntryResponse> createMilkEntry(
            @Valid @RequestBody MilkEntryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(milkEntryService.createMilkEntry(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MilkEntryResponse> getMilkEntryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                milkEntryService.getMilkEntryById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<MilkEntryResponse>>
    getAllMilkEntries() {

        return ResponseEntity.ok(
                milkEntryService.getAllMilkEntries()
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<MilkEntryResponse>>
    getMilkEntriesByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                milkEntryService.getMilkEntriesByFarmer(farmerId)
        );
    }

    @GetMapping("/range")
    public ResponseEntity<List<MilkEntryResponse>>
    getMilkEntriesByDateRange(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        return ResponseEntity.ok(
                milkEntryService.getMilkEntriesByDateRange(from, to)
        );
    }
}