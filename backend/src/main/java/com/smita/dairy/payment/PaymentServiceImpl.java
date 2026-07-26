package com.smita.dairy.payment;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

import com.smita.dairy.payment.dto.PaymentRequest;
import com.smita.dairy.payment.dto.PaymentResponse;
import com.smita.dairy.payment.exception.InvalidPaymentException;
import com.smita.dairy.payment.exception.PaymentNotFoundException;
import com.smita.dairy.payment.mapper.PaymentMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl
        implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final FarmerRepository farmerRepository;
    private final PaymentMapper paymentMapper;
    private final LedgerService ledgerService;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            FarmerRepository farmerRepository,
            PaymentMapper paymentMapper,
            LedgerService ledgerService) {

        this.paymentRepository = paymentRepository;
        this.farmerRepository = farmerRepository;
        this.paymentMapper = paymentMapper;
        this.ledgerService = ledgerService;
    }

    @Override
    public PaymentResponse createPayment(
            PaymentRequest request) {

        Farmer farmer =
                farmerRepository
                        .findById(request.getFarmerId())
                        .orElseThrow(() ->
                                new FarmerNotFoundException(
                                        request.getFarmerId()
                                )
                        );

        if (!farmer.getActive()) {
            throw new InvalidPaymentException(
                    "Cannot create payment for an inactive farmer"
            );
        }

        if (request.getPaymentDate() == null) {
            throw new InvalidPaymentException(
                    "Payment date is required"
            );
        }

        if (request.getAmount() == null
                || request.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPaymentException(
                    "Payment amount must be greater than zero"
            );
        }

        if (request.getPaymentMode() == null) {
            throw new InvalidPaymentException(
                    "Payment mode is required"
            );
        }

        Payment payment =
                paymentMapper.toEntity(request);

        payment.setFarmer(farmer);

        Payment savedPayment =
                paymentRepository.save(payment);

        ledgerService.createEntry(
                farmer,
                savedPayment.getPaymentDate(),
                LedgerEntryType.DEBIT,
                LedgerTransactionType.PAYMENT,
                savedPayment.getId(),
                savedPayment.getAmount(),
                "Payment to farmer - "
                        + savedPayment.getPaymentMode()
        );

        return paymentMapper
                .toResponse(savedPayment);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(
            Long id) {

        Payment payment =
                paymentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new PaymentNotFoundException(id)
                        );

        return paymentMapper.toResponse(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository
                .findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByFarmer(
            Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(
                    farmerId
            );
        }

        return paymentRepository
                .findByFarmerIdOrderByPaymentDateDesc(
                        farmerId
                )
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}