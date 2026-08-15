package com.smita.dairy.settlement;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;

import com.smita.dairy.ledger.LedgerEntry;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerRepository;

import com.smita.dairy.settlement.dto.SettlementRequest;
import com.smita.dairy.settlement.dto.SettlementResponse;
import com.smita.dairy.settlement.exception.InvalidSettlementException;
import com.smita.dairy.settlement.mapper.SettlementMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SettlementServiceImplTest {

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private LedgerRepository ledgerRepository;

    @Mock
    private SettlementMapper settlementMapper;

    @InjectMocks
    private SettlementServiceImpl settlementService;

    private Farmer farmer;
    private SettlementRequest request;

    @BeforeEach
    void setUp() {

        farmer = mock(Farmer.class);

        request = new SettlementRequest();
        request.setFarmerId(2L);
        request.setPeriodFrom(
                LocalDate.of(2026, 7, 26)
        );
        request.setPeriodTo(
                LocalDate.of(2026, 7, 26)
        );
        request.setRemarks(
                "July settlement test"
        );
    }

    @Test
    void shouldCalculateAndCreateSettlement() {

        when(farmer.getId()).thenReturn(2L);

        LedgerEntry credit = new LedgerEntry();
        credit.setEntryType(LedgerEntryType.CREDIT);
        credit.setAmount(new BigDecimal("504.00"));

        LedgerEntry debit1 = new LedgerEntry();
        debit1.setEntryType(LedgerEntryType.DEBIT);
        debit1.setAmount(new BigDecimal("1200.00"));

        LedgerEntry debit2 = new LedgerEntry();
        debit2.setEntryType(LedgerEntryType.DEBIT);
        debit2.setAmount(new BigDecimal("300.00"));

        LedgerEntry debit3 = new LedgerEntry();
        debit3.setEntryType(LedgerEntryType.DEBIT);
        debit3.setAmount(new BigDecimal("200.00"));

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(settlementRepository
                .existsByFarmerIdAndPeriodFromLessThanEqualAndPeriodToGreaterThanEqual(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        LocalDate.of(2026, 7, 26)
                ))
                .thenReturn(false);

        when(ledgerRepository
                .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        LocalDate.of(2026, 7, 26)
                ))
                .thenReturn(
                        List.of(
                                credit,
                                debit1,
                                debit2,
                                debit3
                        )
                );

        when(settlementRepository.save(any(Settlement.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        SettlementResponse response =
                new SettlementResponse();

        when(settlementMapper.toResponse(any(Settlement.class)))
                .thenReturn(response);

        SettlementResponse result =
                settlementService.createSettlement(request);

        assertNotNull(result);

        ArgumentCaptor<Settlement> captor =
                ArgumentCaptor.forClass(Settlement.class);

        verify(settlementRepository)
                .save(captor.capture());

        Settlement saved = captor.getValue();

        assertEquals(
                new BigDecimal("504.00"),
                saved.getTotalCredits()
        );

        assertEquals(
                new BigDecimal("1700.00"),
                saved.getTotalDebits()
        );

        assertEquals(
                new BigDecimal("-1196.00"),
                saved.getNetAmount()
        );

        assertEquals(
                SettlementStatus.FINALIZED,
                saved.getStatus()
        );

        assertEquals(
                farmer,
                saved.getFarmer()
        );

        assertEquals(
                "July settlement test",
                saved.getRemarks()
        );
    }

    @Test
    void shouldRejectOverlappingSettlement() {

        when(farmer.getId()).thenReturn(2L);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(settlementRepository
                .existsByFarmerIdAndPeriodFromLessThanEqualAndPeriodToGreaterThanEqual(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        LocalDate.of(2026, 7, 26)
                ))
                .thenReturn(true);

        assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.createSettlement(request)
        );

        verify(ledgerRepository, never())
                .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                        anyLong(),
                        any(),
                        any()
                );

        verify(settlementRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectSettlementWhenNoTransactionsExist() {

        when(farmer.getId()).thenReturn(2L);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(settlementRepository
                .existsByFarmerIdAndPeriodFromLessThanEqualAndPeriodToGreaterThanEqual(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        LocalDate.of(2026, 7, 26)
                ))
                .thenReturn(false);

        when(ledgerRepository
                .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        LocalDate.of(2026, 7, 26)
                ))
                .thenReturn(List.of());

        assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.createSettlement(request)
        );

        verify(settlementRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectInvalidDateRange() {

        request.setPeriodFrom(
                LocalDate.of(2026, 7, 27)
        );

        request.setPeriodTo(
                LocalDate.of(2026, 7, 26)
        );

        assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.createSettlement(request)
        );

        verifyNoInteractions(
                farmerRepository,
                ledgerRepository
        );

        verify(settlementRepository, never())
                .save(any());
    }
}