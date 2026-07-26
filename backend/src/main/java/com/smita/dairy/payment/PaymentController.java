package com.smita.dairy.payment;

import com.smita.dairy.payment.dto.PaymentRequest;
import com.smita.dairy.payment.dto.PaymentResponse;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(
            @Valid @RequestBody PaymentRequest request) {

        PaymentResponse response =
                paymentService.createPayment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                paymentService.getPaymentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>>
    getAllPayments() {

        return ResponseEntity.ok(
                paymentService.getAllPayments()
        );
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<PaymentResponse>>
    getPaymentsByFarmer(
            @PathVariable Long farmerId) {

        return ResponseEntity.ok(
                paymentService
                        .getPaymentsByFarmer(farmerId)
        );
    }
}