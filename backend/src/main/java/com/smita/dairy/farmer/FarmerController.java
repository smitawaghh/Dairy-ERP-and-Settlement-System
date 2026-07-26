package com.smita.dairy.farmer;

import com.smita.dairy.farmer.dto.FarmerRequest;
import com.smita.dairy.farmer.dto.FarmerResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmers")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(FarmerService farmerService) {
        this.farmerService = farmerService;
    }

    @PostMapping
    public ResponseEntity<FarmerResponse> createFarmer(
            @Valid @RequestBody FarmerRequest request) {

        FarmerResponse response = farmerService.createFarmer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FarmerResponse> getFarmerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                farmerService.getFarmerById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<FarmerResponse>> getAllFarmers() {

        return ResponseEntity.ok(
                farmerService.getAllFarmers()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FarmerResponse> updateFarmer(
            @PathVariable Long id,
            @Valid @RequestBody FarmerRequest request) {

        return ResponseEntity.ok(
                farmerService.updateFarmer(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateFarmer(
            @PathVariable Long id) {

        farmerService.deactivateFarmer(id);

        return ResponseEntity.noContent().build();
    }
}