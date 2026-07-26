package com.smita.dairy.payment;

import com.smita.dairy.payment.dto.PaymentRequest;
import com.smita.dairy.payment.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(
            PaymentRequest request
    );

    PaymentResponse getPaymentById(
            Long id
    );

    List<PaymentResponse> getAllPayments();

    List<PaymentResponse> getPaymentsByFarmer(
            Long farmerId
    );
}