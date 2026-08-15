package com.smita.dairy.farmer;

import com.smita.dairy.common.exception.ApiError;
import com.smita.dairy.farmer.dto.FarmerRequest;
import com.smita.dairy.farmer.dto.FarmerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/farmers")
@Tag(
        name = "Farmers",
        description = "Farmer registration and management APIs"
)
@SecurityRequirement(name = "bearerAuth")
public class FarmerController {

    private final FarmerService farmerService;

    public FarmerController(
            FarmerService farmerService) {

        this.farmerService = farmerService;
    }

    @PostMapping
    @Operation(
            summary = "Create farmer",
            description = "Registers a new farmer in the dairy system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Farmer created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid farmer data",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ApiError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Farmer already exists",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ApiError.class
                            )
                    )
            )
    })
    public ResponseEntity<FarmerResponse> createFarmer(
            @Valid
            @RequestBody
            FarmerRequest request) {

        FarmerResponse response =
                farmerService.createFarmer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get farmer by ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Farmer found"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Farmer not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ApiError.class
                            )
                    )
            )
    })
    public ResponseEntity<FarmerResponse> getFarmerById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                farmerService.getFarmerById(id)
        );
    }

    @GetMapping
    @Operation(
            summary = "Get all farmers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Farmers returned successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            )
    })
    public ResponseEntity<List<FarmerResponse>>
    getAllFarmers() {

        return ResponseEntity.ok(
                farmerService.getAllFarmers()
        );
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update farmer"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Farmer updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid farmer data",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ApiError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Farmer not found",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ApiError.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Farmer data conflicts with existing record"
            )
    })
    public ResponseEntity<FarmerResponse> updateFarmer(
            @PathVariable Long id,
            @Valid
            @RequestBody
            FarmerRequest request) {

        return ResponseEntity.ok(
                farmerService.updateFarmer(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deactivate farmer",
            description = "Soft deactivates a farmer instead of deleting historical records."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Farmer deactivated successfully"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Farmer not found"
            )
    })
    public ResponseEntity<Void> deactivateFarmer(
            @PathVariable Long id) {

        farmerService.deactivateFarmer(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}