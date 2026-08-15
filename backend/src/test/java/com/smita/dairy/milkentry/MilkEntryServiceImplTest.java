package com.smita.dairy.milkentry;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;

import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

import com.smita.dairy.milkentry.dto.MilkEntryRequest;
import com.smita.dairy.milkentry.dto.MilkEntryResponse;
import com.smita.dairy.milkentry.exception.InvalidMilkEntryException;
import com.smita.dairy.milkentry.mapper.MilkEntryMapper;

import com.smita.dairy.ratecard.MilkType;
import com.smita.dairy.ratecard.RateCard;
import com.smita.dairy.ratecard.RateCardRepository;

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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilkEntryServiceImplTest {

    @Mock
    private MilkEntryRepository milkEntryRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private RateCardRepository rateCardRepository;

    @Mock
    private MilkEntryMapper milkEntryMapper;

    @Mock
    private LedgerService ledgerService;

    @InjectMocks
    private MilkEntryServiceImpl milkEntryService;

    private Farmer farmer;
    private RateCard rateCard;
    private MilkEntryRequest request;
    private MilkEntry milkEntry;
    private MilkEntry savedEntry;
    private MilkEntryResponse response;

    @BeforeEach
    void setUp() {

        farmer = new Farmer();
        farmer.setFarmerCode("F0002");
        farmer.setFullName("Suresh Patil");
        farmer.setActive(true);

        rateCard = new RateCard();
        rateCard.setFatRate(new BigDecimal("5.00"));
        rateCard.setSnfRate(new BigDecimal("2.00"));

        request = new MilkEntryRequest();
        request.setFarmerId(2L);
        request.setCollectionDate(
                LocalDate.of(2026, 7, 26)
        );
        request.setCollectionShift(
                CollectionShift.EVENING
        );
        request.setMilkType(MilkType.COW);
        request.setQuantity(
                new BigDecimal("10.00")
        );
        request.setFat(
                new BigDecimal("4.00")
        );
        request.setSnf(
                new BigDecimal("8.00")
        );

        milkEntry = new MilkEntry();
        savedEntry = new MilkEntry();

        response = new MilkEntryResponse();
    }

    @Test
    void shouldCreateMilkEntryAndCreditLedger() {

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(milkEntryRepository
                .existsByFarmerIdAndCollectionDateAndCollectionShiftAndMilkType(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        CollectionShift.EVENING,
                        MilkType.COW
                ))
                .thenReturn(false);

        when(rateCardRepository.findApplicableRateCard(
                MilkType.COW,
                LocalDate.of(2026, 7, 26)
        )).thenReturn(Optional.of(rateCard));

        when(milkEntryMapper.toEntity(request))
                .thenReturn(milkEntry);

        when(milkEntryRepository.save(milkEntry))
                .thenReturn(savedEntry);

        when(milkEntryMapper.toResponse(savedEntry))
                .thenReturn(response);

        MilkEntryResponse result =
                milkEntryService.createMilkEntry(request);

        assertNotNull(result);

        /*
         * FAT amount = 4 × 5 = 20
         * SNF amount = 8 × 2 = 16
         *
         * Rate/litre = 36
         *
         * Quantity = 10 litres
         *
         * Total = 360
         */
        assertEquals(
                new BigDecimal("36.0000"),
                milkEntry.getRatePerLitre()
        );

        assertEquals(
                new BigDecimal("360.00"),
                milkEntry.getAmount()
        );

        verify(milkEntryRepository)
                .save(milkEntry);

        verify(ledgerService)
                .createEntry(
                        eq(farmer),
                        eq(LocalDate.of(2026, 7, 26)),
                        eq(LedgerEntryType.CREDIT),
                        eq(LedgerTransactionType.MILK_ENTRY),
                        any(),
                        eq(new BigDecimal("360.00")),
                        eq("Milk collection - EVENING - COW")
                );
    }

    @Test
    void shouldRejectDuplicateMilkEntry() {

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(milkEntryRepository
                .existsByFarmerIdAndCollectionDateAndCollectionShiftAndMilkType(
                        2L,
                        LocalDate.of(2026, 7, 26),
                        CollectionShift.EVENING,
                        MilkType.COW
                ))
                .thenReturn(true);

        assertThrows(
                InvalidMilkEntryException.class,
                () -> milkEntryService
                        .createMilkEntry(request)
        );

        verify(milkEntryRepository, never())
                .save(any());

        verify(ledgerService, never())
                .createEntry(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void shouldRejectInactiveFarmer() {

        farmer.setActive(false);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        assertThrows(
                InvalidMilkEntryException.class,
                () -> milkEntryService
                        .createMilkEntry(request)
        );

        verify(milkEntryRepository, never())
                .save(any());

        verify(ledgerService, never())
                .createEntry(
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any(),
                        any()
                );
    }
}