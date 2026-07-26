package com.smita.dairy.milkentry;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

import com.smita.dairy.milkentry.dto.MilkEntryRequest;
import com.smita.dairy.milkentry.dto.MilkEntryResponse;
import com.smita.dairy.milkentry.exception.InvalidMilkEntryException;
import com.smita.dairy.milkentry.exception.MilkEntryNotFoundException;
import com.smita.dairy.milkentry.mapper.MilkEntryMapper;

import com.smita.dairy.ratecard.RateCard;
import com.smita.dairy.ratecard.RateCardRepository;
import com.smita.dairy.ratecard.exception.RateCardNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MilkEntryServiceImpl implements MilkEntryService {

    private final MilkEntryRepository milkEntryRepository;
    private final FarmerRepository farmerRepository;
    private final RateCardRepository rateCardRepository;
    private final MilkEntryMapper milkEntryMapper;
    private final LedgerService ledgerService;

    public MilkEntryServiceImpl(
            MilkEntryRepository milkEntryRepository,
            FarmerRepository farmerRepository,
            RateCardRepository rateCardRepository,
            MilkEntryMapper milkEntryMapper,
            LedgerService ledgerService) {

        this.milkEntryRepository = milkEntryRepository;
        this.farmerRepository = farmerRepository;
        this.rateCardRepository = rateCardRepository;
        this.milkEntryMapper = milkEntryMapper;
        this.ledgerService = ledgerService;
    }

    @Override
    public MilkEntryResponse createMilkEntry(
            MilkEntryRequest request) {

        Farmer farmer = farmerRepository
                .findById(request.getFarmerId())
                .orElseThrow(() ->
                        new FarmerNotFoundException(
                                request.getFarmerId()
                        )
                );

        if (!farmer.getActive()) {
            throw new InvalidMilkEntryException(
                    "Cannot record milk for an inactive farmer"
            );
        }

        boolean duplicate =
                milkEntryRepository
                        .existsByFarmerIdAndCollectionDateAndCollectionShiftAndMilkType(
                                request.getFarmerId(),
                                request.getCollectionDate(),
                                request.getCollectionShift(),
                                request.getMilkType()
                        );

        if (duplicate) {
            throw new InvalidMilkEntryException(
                    "Milk entry already exists for this farmer, date, shift and milk type"
            );
        }

        RateCard rateCard =
                rateCardRepository
                        .findApplicableRateCard(
                                request.getMilkType(),
                                request.getCollectionDate()
                        )
                        .orElseThrow(() ->
                                new RateCardNotFoundException(
                                        "No applicable rate card found for "
                                                + request.getMilkType()
                                                + " on "
                                                + request.getCollectionDate()
                                )
                        );

        BigDecimal fatAmount =
                request.getFat()
                        .multiply(rateCard.getFatRate());

        BigDecimal snfAmount =
                request.getSnf()
                        .multiply(rateCard.getSnfRate());

        BigDecimal ratePerLitre =
                fatAmount
                        .add(snfAmount)
                        .setScale(4, RoundingMode.HALF_UP);

        BigDecimal totalAmount =
                ratePerLitre
                        .multiply(request.getQuantity())
                        .setScale(2, RoundingMode.HALF_UP);

        MilkEntry entry =
                milkEntryMapper.toEntity(request);

        entry.setFarmer(farmer);

        entry.setFatRate(rateCard.getFatRate());
        entry.setSnfRate(rateCard.getSnfRate());

        entry.setRatePerLitre(ratePerLitre);
        entry.setAmount(totalAmount);

        MilkEntry savedEntry =
                milkEntryRepository.save(entry);

        ledgerService.createEntry(
                farmer,
                request.getCollectionDate(),
                LedgerEntryType.CREDIT,
                LedgerTransactionType.MILK_ENTRY,
                savedEntry.getId(),
                totalAmount,
                "Milk collection - "
                        + request.getCollectionShift()
                        + " - "
                        + request.getMilkType()
        );

        return milkEntryMapper.toResponse(savedEntry);
    }

    @Override
    @Transactional(readOnly = true)
    public MilkEntryResponse getMilkEntryById(Long id) {

        MilkEntry entry =
                milkEntryRepository.findById(id)
                        .orElseThrow(() ->
                                new MilkEntryNotFoundException(id)
                        );

        return milkEntryMapper.toResponse(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkEntryResponse> getAllMilkEntries() {

        return milkEntryRepository.findAll()
                .stream()
                .map(milkEntryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkEntryResponse> getMilkEntriesByFarmer(
            Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(farmerId);
        }

        return milkEntryRepository
                .findByFarmerIdOrderByCollectionDateDesc(farmerId)
                .stream()
                .map(milkEntryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MilkEntryResponse> getMilkEntriesByDateRange(
            LocalDate from,
            LocalDate to) {

        if (from.isAfter(to)) {
            throw new InvalidMilkEntryException(
                    "From date cannot be after to date"
            );
        }

        return milkEntryRepository
                .findByCollectionDateBetweenOrderByCollectionDateDesc(
                        from,
                        to
                )
                .stream()
                .map(milkEntryMapper::toResponse)
                .toList();
    }
}