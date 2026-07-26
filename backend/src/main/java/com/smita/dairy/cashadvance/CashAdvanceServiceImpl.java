package com.smita.dairy.cashadvance;

import com.smita.dairy.cashadvance.dto.CashAdvanceRequest;
import com.smita.dairy.cashadvance.dto.CashAdvanceResponse;
import com.smita.dairy.cashadvance.exception.CashAdvanceNotFoundException;
import com.smita.dairy.cashadvance.exception.InvalidCashAdvanceException;
import com.smita.dairy.cashadvance.mapper.CashAdvanceMapper;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;

import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerService;
import com.smita.dairy.ledger.LedgerTransactionType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CashAdvanceServiceImpl
        implements CashAdvanceService {

    private final CashAdvanceRepository cashAdvanceRepository;
    private final FarmerRepository farmerRepository;
    private final CashAdvanceMapper cashAdvanceMapper;
    private final LedgerService ledgerService;

    public CashAdvanceServiceImpl(
            CashAdvanceRepository cashAdvanceRepository,
            FarmerRepository farmerRepository,
            CashAdvanceMapper cashAdvanceMapper,
            LedgerService ledgerService) {

        this.cashAdvanceRepository =
                cashAdvanceRepository;

        this.farmerRepository =
                farmerRepository;

        this.cashAdvanceMapper =
                cashAdvanceMapper;

        this.ledgerService =
                ledgerService;
    }

    @Override
    public CashAdvanceResponse createCashAdvance(
            CashAdvanceRequest request) {

        Farmer farmer =
                farmerRepository
                        .findById(request.getFarmerId())
                        .orElseThrow(() ->
                                new FarmerNotFoundException(
                                        request.getFarmerId()
                                )
                        );

        if (!farmer.getActive()) {
            throw new InvalidCashAdvanceException(
                    "Cannot issue cash advance to an inactive farmer"
            );
        }

        if (request.getAmount() == null
                || request.getAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidCashAdvanceException(
                    "Cash advance amount must be greater than zero"
            );
        }

        if (request.getAdvanceDate() == null) {
            throw new InvalidCashAdvanceException(
                    "Advance date is required"
            );
        }

        CashAdvance cashAdvance =
                cashAdvanceMapper.toEntity(request);

        cashAdvance.setFarmer(farmer);

        CashAdvance savedCashAdvance =
                cashAdvanceRepository.save(cashAdvance);

        ledgerService.createEntry(
                farmer,
                savedCashAdvance.getAdvanceDate(),
                LedgerEntryType.DEBIT,
                LedgerTransactionType.CASH_ADVANCE,
                savedCashAdvance.getId(),
                savedCashAdvance.getAmount(),
                "Cash advance"
        );

        return cashAdvanceMapper
                .toResponse(savedCashAdvance);
    }

    @Override
    @Transactional(readOnly = true)
    public CashAdvanceResponse getCashAdvanceById(
            Long id) {

        CashAdvance cashAdvance =
                cashAdvanceRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new CashAdvanceNotFoundException(id)
                        );

        return cashAdvanceMapper
                .toResponse(cashAdvance);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashAdvanceResponse>
    getAllCashAdvances() {

        return cashAdvanceRepository
                .findAll()
                .stream()
                .map(cashAdvanceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashAdvanceResponse>
    getCashAdvancesByFarmer(
            Long farmerId) {

        if (!farmerRepository.existsById(farmerId)) {
            throw new FarmerNotFoundException(farmerId);
        }

        return cashAdvanceRepository
                .findByFarmerIdOrderByAdvanceDateDesc(
                        farmerId
                )
                .stream()
                .map(cashAdvanceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashAdvanceResponse>
    getCashAdvancesByDateRange(
            LocalDate from,
            LocalDate to) {

        if (from == null || to == null) {
            throw new InvalidCashAdvanceException(
                    "From and to dates are required"
            );
        }

        if (from.isAfter(to)) {
            throw new InvalidCashAdvanceException(
                    "From date cannot be after to date"
            );
        }

        return cashAdvanceRepository
                .findByAdvanceDateBetweenOrderByAdvanceDateDesc(
                        from,
                        to
                )
                .stream()
                .map(cashAdvanceMapper::toResponse)
                .toList();
    }
}