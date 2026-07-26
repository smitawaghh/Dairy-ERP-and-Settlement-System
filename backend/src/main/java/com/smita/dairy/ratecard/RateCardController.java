package com.smita.dairy.ratecard;

import com.smita.dairy.ratecard.dto.RateCardRequest;
import com.smita.dairy.ratecard.dto.RateCardResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rate-cards")
public class RateCardController {

    private final RateCardService rateCardService;

    public RateCardController(RateCardService rateCardService) {
        this.rateCardService = rateCardService;
    }

    // CREATE RATE CARD
    @PostMapping
    public ResponseEntity<RateCardResponse> createRateCard(
            @Valid @RequestBody RateCardRequest request) {

        RateCardResponse response =
                rateCardService.createRateCard(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // GET RATE CARD BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RateCardResponse> getRateCardById(
            @PathVariable Long id) {

        RateCardResponse response =
                rateCardService.getRateCardById(id);

        return ResponseEntity.ok(response);
    }

    // GET ALL RATE CARDS
    @GetMapping
    public ResponseEntity<List<RateCardResponse>> getAllRateCards() {

        List<RateCardResponse> response =
                rateCardService.getAllRateCards();

        return ResponseEntity.ok(response);
    }

    // GET APPLICABLE RATE CARD FOR A DATE
    @GetMapping("/applicable")
    public ResponseEntity<RateCardResponse> getApplicableRateCard(
            @RequestParam MilkType milkType,
            @RequestParam LocalDate date) {

        RateCardResponse response =
                rateCardService.getApplicableRateCard(
                        milkType,
                        date
                );

        return ResponseEntity.ok(response);
    }
}