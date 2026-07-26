package com.smita.dairy.settlement;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.ledger.LedgerEntry;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerRepository;

import com.smita.dairy.settlement.dto.SettlementRequest;
import com.smita.dairy.settlement.dto.SettlementResponse;
import com.smita.dairy.settlement.exception.InvalidSettlementException;
import com.smita.dairy.settlement.exception.SettlementNotFoundException;
import com.smita.dairy.settlement.mapper.SettlementMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class SettlementServiceImpl
        implements SettlementService {

    private final SettlementRepository settlementRepository;
    private final FarmerRepository farmerRepository;
    private final LedgerRepository ledgerRepository;
    private final SettlementMapper settlementMapper;

    public SettlementServiceImpl(
            SettlementRepository settlementRepository,
            FarmerRepository farmerRepository,
            LedgerRepository ledgerRepository,
            SettlementMapper settlementMapper) {

        this.settlementRepository = settlementRepository;
        this.farmerRepository = farmerRepository;
        this.ledgerRepository = ledgerRepository;
        this.settlementMapper = settlementMapper;
    }

    @Override
    public SettlementResponse createSettlement(
            SettlementRequest request) {

        if (request.getPeriodFrom() == null
                || request.getPeriodTo() == null) {

            throw new InvalidSettlementException(
                    "Settlement period dates are required"
            );
        }

        if (request.getPeriodFrom()
                .isAfter(request.getPeriodTo())) {

            throw new InvalidSettlementException(
                    "Period from date cannot be after period to date"
            );
        }

        Farmer farmer =
                farmerRepository
                        .findById(request.getFarmerId())
                        .orElseThrow(() ->
                                new FarmerNotFoundException(
                                        request.getFarmerId()
                                )
                        );

        boolean overlaps =
                settlementRepository
                        .existsByFarmerIdAndPeriodFromLessThanEqualAndPeriodToGreaterThanEqual(
                                farmer.getId(),
                                request.getPeriodTo(),
                                request.getPeriodFrom()
                        );

        if (overlaps) {
            throw new InvalidSettlementException(
                    "Settlement period overlaps with an existing settlement for this farmer"
            );
        }

        List<LedgerEntry> entries =
                ledgerRepository
                        .findByFarmerIdAndTransactionDateBetweenOrderByTransactionDateDescIdDesc(
                                farmer.getId(),
                                request.getPeriodFrom(),
                                request.getPeriodTo()
                        );

        if (entries.isEmpty()) {
            throw new InvalidSettlementException(
                    "No ledger transactions found for the selected settlement period"
            );
        }

        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal totalDebits = BigDecimal.ZERO;

        for (LedgerEntry entry : entries) {

            if (entry.getEntryType()
                    == LedgerEntryType.CREDIT) {

                totalCredits =
                        totalCredits.add(
                                entry.getAmount()
                        );

            } else if (entry.getEntryType()
                    == LedgerEntryType.DEBIT) {

                totalDebits =
                        totalDebits.add(
                                entry.getAmount()
                        );
            }
        }

        totalCredits =
                totalCredits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        totalDebits =
                totalDebits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal netAmount =
                totalCredits
                        .subtract(totalDebits)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        Settlement settlement =
                new Settlement();

        settlement.setFarmer(farmer);

        settlement.setPeriodFrom(
                request.getPeriodFrom()
        );

        settlement.setPeriodTo(
                request.getPeriodTo()
        );

        settlement.setTotalCredits(
                totalCredits
        );

        settlement.setTotalDebits(
                totalDebits
        );

        settlement.setNetAmount(
                netAmount
        );

        settlement.setStatus(
                SettlementStatus.FINALIZED
        );

        settlement.setRemarks(
                request.getRemarks()
        );

        Settlement savedSettlement =
                settlementRepository.save(
                        settlement
                );

        return settlementMapper
                .toResponse(savedSettlement);
    }

    @Override
    @Transactional(readOnly = true)
    public SettlementResponse getSettlementById(
            Long id) {

        Settlement settlement =
                settlementRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new SettlementNotFoundException(id)
                        );

        return settlementMapper
                .toResponse(settlement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettlementResponse>
    getAllSettlements() {

        return settlementRepository
                .findAll()
                .stream()
                .map(settlementMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SettlementResponse>
    getSettlementsByFarmer(
            Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(
                    farmerId
            );
        }

        return settlementRepository
                .findByFarmerIdOrderByPeriodFromDesc(
                        farmerId
                )
                .stream()
                .map(settlementMapper::toResponse)
                .toList();
    }
}