package com.smita.dairy.dashboard;

import com.smita.dairy.dashboard.dto.DashboardSummaryResponse;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.farmer.FarmerRepository;

import com.smita.dairy.ledger.LedgerEntry;
import com.smita.dairy.ledger.LedgerEntryType;
import com.smita.dairy.ledger.LedgerRepository;

import com.smita.dairy.milkentry.MilkEntry;
import com.smita.dairy.milkentry.MilkEntryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardServiceImpl
        implements DashboardService {

    private final FarmerRepository farmerRepository;
    private final MilkEntryRepository milkEntryRepository;
    private final LedgerRepository ledgerRepository;

    public DashboardServiceImpl(
            FarmerRepository farmerRepository,
            MilkEntryRepository milkEntryRepository,
            LedgerRepository ledgerRepository) {

        this.farmerRepository = farmerRepository;
        this.milkEntryRepository = milkEntryRepository;
        this.ledgerRepository = ledgerRepository;
    }

    @Override
    public DashboardSummaryResponse getSummary(
            LocalDate date) {

        LocalDate targetDate =
                date != null
                        ? date
                        : LocalDate.now();

        List<Farmer> farmers =
                farmerRepository.findAll();

        long totalFarmers =
                farmers.size();

        long activeFarmers =
                farmers.stream()
                        .filter(farmer ->
                                Boolean.TRUE.equals(
                                        farmer.getActive()
                                )
                        )
                        .count();

        List<MilkEntry> milkEntries =
                milkEntryRepository
                        .findByCollectionDateBetweenOrderByCollectionDateDesc(
                                targetDate,
                                targetDate
                        );

        BigDecimal milkQuantity =
                BigDecimal.ZERO;

        BigDecimal milkValue =
                BigDecimal.ZERO;

        for (MilkEntry entry : milkEntries) {

            milkQuantity =
                    milkQuantity.add(
                            entry.getQuantity()
                    );

            milkValue =
                    milkValue.add(
                            entry.getAmount()
                    );
        }

        List<LedgerEntry> allLedgerEntries =
                ledgerRepository.findAll();

        BigDecimal dailyCredits =
                BigDecimal.ZERO;

        BigDecimal dailyDebits =
                BigDecimal.ZERO;

        BigDecimal overallBalance =
                BigDecimal.ZERO;

        for (LedgerEntry entry : allLedgerEntries) {

            if (entry.getEntryType()
                    == LedgerEntryType.CREDIT) {

                overallBalance =
                        overallBalance.add(
                                entry.getAmount()
                        );

                if (entry.getTransactionDate()
                        .equals(targetDate)) {

                    dailyCredits =
                            dailyCredits.add(
                                    entry.getAmount()
                            );
                }

            } else if (entry.getEntryType()
                    == LedgerEntryType.DEBIT) {

                overallBalance =
                        overallBalance.subtract(
                                entry.getAmount()
                        );

                if (entry.getTransactionDate()
                        .equals(targetDate)) {

                    dailyDebits =
                            dailyDebits.add(
                                    entry.getAmount()
                            );
                }
            }
        }

        milkQuantity =
                milkQuantity.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        milkValue =
                milkValue.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        dailyCredits =
                dailyCredits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        dailyDebits =
                dailyDebits.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        overallBalance =
                overallBalance.setScale(
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal dailyNet =
                dailyCredits
                        .subtract(dailyDebits)
                        .setScale(
                                2,
                                RoundingMode.HALF_UP
                        );

        DashboardSummaryResponse response =
                new DashboardSummaryResponse();

        response.setDate(targetDate);

        response.setTotalFarmers(
                totalFarmers
        );

        response.setActiveFarmers(
                activeFarmers
        );

        response.setMilkEntryCount(
                milkEntries.size()
        );

        response.setMilkQuantity(
                milkQuantity
        );

        response.setMilkValue(
                milkValue
        );

        response.setCredits(
                dailyCredits
        );

        response.setDebits(
                dailyDebits
        );

        response.setDailyNetAmount(
                dailyNet
        );

        response.setOverallLedgerBalance(
                overallBalance
        );

        return response;
    }
}