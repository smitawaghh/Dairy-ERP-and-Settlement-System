package com.smita.dairy.feedsale;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.feedsale.dto.FeedSaleRequest;
import com.smita.dairy.feedsale.dto.FeedSaleResponse;
import com.smita.dairy.feedsale.exception.InvalidFeedSaleException;
import com.smita.dairy.feedsale.mapper.FeedSaleMapper;
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
class FeedSaleServiceImplTest {

    @Mock
    private FeedSaleRepository feedSaleRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @Mock
    private FeedSaleMapper feedSaleMapper;

    @Mock
    private LedgerService ledgerService;

    @InjectMocks
    private FeedSaleServiceImpl feedSaleService;

    private Farmer farmer;
    private FeedSaleRequest request;

    @BeforeEach
    void setUp() {

        farmer = new Farmer();
        farmer.setActive(true);

        request = new FeedSaleRequest();
        request.setFarmerId(2L);
        request.setSaleDate(LocalDate.of(2026, 7, 26));
        request.setFeedName("Cattle Feed 50kg");
        request.setQuantity(new BigDecimal("2.000"));
        request.setUnitPrice(new BigDecimal("600.00"));
    }

    @Test
    void shouldCreateFeedSaleAndDebitLedger() {

        FeedSale feedSale = new FeedSale();
        feedSale.setSaleDate(request.getSaleDate());
        feedSale.setFeedName(request.getFeedName());
        feedSale.setQuantity(request.getQuantity());
        feedSale.setUnitPrice(request.getUnitPrice());

        FeedSaleResponse response = new FeedSaleResponse();

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        when(feedSaleMapper.toEntity(request))
                .thenReturn(feedSale);

        when(feedSaleRepository.save(feedSale))
                .thenReturn(feedSale);

        when(feedSaleMapper.toResponse(feedSale))
                .thenReturn(response);

        FeedSaleResponse result =
                feedSaleService.createFeedSale(request);

        assertNotNull(result);

        assertEquals(
                new BigDecimal("1200.00"),
                feedSale.getTotalAmount()
        );

        assertEquals(farmer, feedSale.getFarmer());

        verify(feedSaleRepository).save(feedSale);

        verify(ledgerService).createEntry(
                eq(farmer),
                eq(LocalDate.of(2026, 7, 26)),
                eq(LedgerEntryType.DEBIT),
                eq(LedgerTransactionType.FEED_SALE),
                any(),
                eq(new BigDecimal("1200.00")),
                eq("Feed sale - Cattle Feed 50kg")
        );
    }

    @Test
    void shouldRejectInactiveFarmer() {

        farmer.setActive(false);

        when(farmerRepository.findById(2L))
                .thenReturn(Optional.of(farmer));

        assertThrows(
                InvalidFeedSaleException.class,
                () -> feedSaleService.createFeedSale(request)
        );

        verify(feedSaleRepository, never()).save(any());
        verifyNoInteractions(ledgerService);
    }
}