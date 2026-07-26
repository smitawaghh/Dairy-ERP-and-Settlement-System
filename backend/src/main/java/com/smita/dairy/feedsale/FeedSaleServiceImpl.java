package com.smita.dairy.feedsale;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.feedsale.dto.FeedSaleRequest;
import com.smita.dairy.feedsale.dto.FeedSaleResponse;
import com.smita.dairy.feedsale.exception.InvalidFeedSaleException;
import com.smita.dairy.feedsale.exception.FeedSaleNotFoundException;
import com.smita.dairy.feedsale.mapper.FeedSaleMapper;

import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class FeedSaleServiceImpl implements FeedSaleService {

    private final FeedSaleRepository feedSaleRepository;
    private final FarmerRepository farmerRepository;
    private final FeedSaleMapper feedSaleMapper;
    private final LedgerService ledgerService;

    public FeedSaleServiceImpl(
            FeedSaleRepository feedSaleRepository,
            FarmerRepository farmerRepository,
            FeedSaleMapper feedSaleMapper,
            LedgerService ledgerService) {

        this.feedSaleRepository = feedSaleRepository;
        this.farmerRepository = farmerRepository;
        this.feedSaleMapper = feedSaleMapper;
        this.ledgerService = ledgerService;
    }

    @Override
    public FeedSaleResponse createFeedSale(FeedSaleRequest request) {

        Farmer farmer = farmerRepository
                .findById(request.getFarmerId())
                .orElseThrow(() ->
                        new FarmerNotFoundException(request.getFarmerId())
                );

        if (!farmer.getActive()) {
            throw new InvalidFeedSaleException(
                    "Cannot record feed sale for an inactive farmer"
            );
        }

        BigDecimal totalAmount =
                request.getQuantity()
                        .multiply(request.getUnitPrice())
                        .setScale(2, RoundingMode.HALF_UP);

        FeedSale feedSale =
                feedSaleMapper.toEntity(request);

        feedSale.setFarmer(farmer);
        feedSale.setTotalAmount(totalAmount);

        FeedSale savedFeedSale =
                feedSaleRepository.save(feedSale);

        ledgerService.createEntry(
                farmer,
                request.getSaleDate(),
                LedgerEntryType.DEBIT,
                LedgerTransactionType.FEED_SALE,
                savedFeedSale.getId(),
                totalAmount,
                "Feed sale - " + savedFeedSale.getFeedName()
        );

        return feedSaleMapper.toResponse(savedFeedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public FeedSaleResponse getFeedSaleById(Long id) {

        FeedSale feedSale =
                feedSaleRepository.findById(id)
                        .orElseThrow(() ->
                                new FeedSaleNotFoundException(id)
                        );

        return feedSaleMapper.toResponse(feedSale);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedSaleResponse> getAllFeedSales() {

        return feedSaleRepository.findAll()
                .stream()
                .map(feedSaleMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FeedSaleResponse> getFeedSalesByFarmer(
            Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(farmerId);
        }

        return feedSaleRepository
                .findByFarmerIdOrderBySaleDateDesc(farmerId)
                .stream()
                .map(feedSaleMapper::toResponse)
                .toList();
    }
}