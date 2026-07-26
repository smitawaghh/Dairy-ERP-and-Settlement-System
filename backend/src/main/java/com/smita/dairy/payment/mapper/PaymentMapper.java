package com.smita.dairy.payment.mapper;

import com.smita.dairy.payment.Payment;
import com.smita.dairy.payment.dto.PaymentRequest;
import com.smita.dairy.payment.dto.PaymentResponse;

import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentRequest request) {

        Payment payment = new Payment();

        payment.setPaymentDate(
                request.getPaymentDate()
        );

        payment.setAmount(
                request.getAmount()
        );

        payment.setPaymentMode(
                request.getPaymentMode()
        );

        payment.setReferenceNumber(
                request.getReferenceNumber()
        );

        payment.setRemarks(
                request.getRemarks()
        );

        return payment;
    }

    public PaymentResponse toResponse(Payment payment) {

        PaymentResponse response =
                new PaymentResponse();

        response.setId(payment.getId());

        response.setFarmerId(
                payment.getFarmer().getId()
        );

        response.setFarmerCode(
                payment.getFarmer().getFarmerCode()
        );

        response.setFarmerName(
                payment.getFarmer().getFullName()
        );

        response.setPaymentDate(
                payment.getPaymentDate()
        );

        response.setAmount(
                payment.getAmount()
        );

        response.setPaymentMode(
                payment.getPaymentMode()
        );

        response.setReferenceNumber(
                payment.getReferenceNumber()
        );

        response.setRemarks(
                payment.getRemarks()
        );

        response.setCreatedAt(
                payment.getCreatedAt()
        );

        return response;
    }
}