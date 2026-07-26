package com.smita.dairy.feedsale;

import com.smita.dairy.feedsale.dto.FeedSaleRequest;
import com.smita.dairy.feedsale.dto.FeedSaleResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed-sales")
public class FeedSaleController {

    private final FeedSaleService feedSaleService;

    public FeedSaleController(
            FeedSaleService feedSaleService) {

        this.feedSaleService = feedSaleService;
    }

    @PostMapping
    public ResponseEntity<FeedSaleResponse> createFeedSale(
            @Valid @RequestBody FeedSaleRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(feedSaleService.createFeedSale(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedSaleResponse> getFeedSaleById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                feedSaleService.getFeedSaleById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<FeedSaleResponse>> getAllFeedSales() {

        return ResponseEntity.ok(
                feedSaleService.getAllFeedSales()
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<FeedSaleResponse>> getFeedSalesByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                feedSaleService.getFeedSalesByFarmer(farmerId)
        );
    }
}