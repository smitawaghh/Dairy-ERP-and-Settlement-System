package com.smita.dairy.cashadvance;

import com.smita.dairy.cashadvance.dto.CashAdvanceRequest;
import com.smita.dairy.cashadvance.dto.CashAdvanceResponse;
import com.smita.dairy.cashadvance.exception.InvalidCashAdvanceException;
import com.smita.dairy.cashadvance.mapper.CashAdvanceMapper;
import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

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
class CashAdvanceServiceImplTest {

    @Mock
    private CashAdvanceRepository cashAdvanceRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private CashAdvanceMapper cashAdvanceMapper;

    @Mock
    private LedgerService ledgerService;

    @InjectMocks
    private CashAdvanceServiceImpl cashAdvanceService;

    private Farmer farmer;
    private CashAdvanceRequest request;

    @BeforeEach
    void setUp() {

        farmer = new Farmer();
        farmer.setActive(true);

        request = new CashAdvanceRequest();
        request.setFarmerId(2L);
        request.setAdvanceDate(LocalDate.of(2026, 7, 26));
        request.setAmount(new BigDecimal("500.00"));
    }

    @Test
    void shouldCreateCashAdvanceAndDebitLedger() {

        CashAdvance cashAdvance = new CashAdvance();
        cashAdvance.setAdvanceDate(request.getAdvanceDate());
        cashAdvance.setAmount(request.getAmount());

        CashAdvance saved = new CashAdvance();
        saved.setAdvanceDate(request.getAdvanceDate());
        saved.setAmount(request.getAmount());

        CashAdvanceResponse response = new CashAdvanceResponse();

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(cashAdvanceMapper.toEntity(request))
                .thenReturn(cashAdvance);

        when(cashAdvanceRepository.save(cashAdvance))
                .thenReturn(saved);

        when(cashAdvanceMapper.toResponse(saved))
                .thenReturn(response);

        CashAdvanceResponse result =
                cashAdvanceService.createCashAdvance(request);

        assertNotNull(result);
        assertEquals(farmer, cashAdvance.getFarmer());

        verify(cashAdvanceRepository).save(cashAdvance);

        verify(ledgerService).createEntry(
                eq(farmer),
                eq(LocalDate.of(2026, 7, 26)),
                eq(LedgerEntryType.DEBIT),
                eq(LedgerTransactionType.CASH_ADVANCE),
                any(),
                eq(new BigDecimal("500.00")),
                eq("Cash advance")
        );
    }

    @Test
    void shouldRejectInactiveFarmer() {

        farmer.setActive(false);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        assertThrows(
                InvalidCashAdvanceException.class,
                () -> cashAdvanceService.createCashAdvance(request)
        );

        verify(cashAdvanceRepository, never()).save(any());
        verifyNoInteractions(ledgerService);
    }
}