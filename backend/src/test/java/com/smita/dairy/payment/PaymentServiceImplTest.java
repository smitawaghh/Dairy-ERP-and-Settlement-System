package com.smita.dairy.payment;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;
import com.smita.dairy.payment.dto.PaymentRequest;
import com.smita.dairy.payment.dto.PaymentResponse;
import com.smita.dairy.payment.exception.InvalidPaymentException;
import com.smita.dairy.payment.mapper.PaymentMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private LedgerService ledgerService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Farmer farmer;
    private PaymentRequest request;

    @BeforeEach
    void setUp() {

        farmer = new Farmer();
        farmer.setActive(true);

        request = new PaymentRequest();
        request.setFarmerId(2L);
        request.setPaymentDate(LocalDate.of(2026, 7, 26));
        request.setAmount(new BigDecimal("300.00"));
        request.setPaymentMode(PaymentMode.UPI);
        request.setReferenceNumber("UPI-TEST-001");
    }

    @Test
    void shouldCreatePaymentAndDebitLedger() {

        Payment payment = new Payment();
        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmount(request.getAmount());
        payment.setPaymentMode(request.getPaymentMode());

        Payment saved = new Payment();
        saved.setId(1L);
        saved.setPaymentDate(request.getPaymentDate());
        saved.setAmount(request.getAmount());
        saved.setPaymentMode(request.getPaymentMode());

        PaymentResponse response = new PaymentResponse();

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(paymentMapper.toEntity(request))
                .thenReturn(payment);

        when(paymentRepository.save(payment))
                .thenReturn(saved);

        when(paymentMapper.toResponse(saved))
                .thenReturn(response);

        PaymentResponse result =
                paymentService.createPayment(request);

        assertNotNull(result);
        assertEquals(farmer, payment.getFarmer());

        verify(paymentRepository).save(payment);

        verify(ledgerService).createEntry(
                eq(farmer),
                eq(LocalDate.of(2026, 7, 26)),
                eq(LedgerEntryType.DEBIT),
                eq(LedgerTransactionType.PAYMENT),
                eq(1L),
                eq(new BigDecimal("300.00")),
                eq("Payment to farmer - UPI")
        );
    }

    @Test
    void shouldRejectZeroPayment() {

        request.setAmount(BigDecimal.ZERO);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        assertThrows(
                InvalidPaymentException.class,
                () -> paymentService.createPayment(request)
        );

        verify(paymentRepository, never()).save(any());
        verifyNoInteractions(ledgerService);
    }
}