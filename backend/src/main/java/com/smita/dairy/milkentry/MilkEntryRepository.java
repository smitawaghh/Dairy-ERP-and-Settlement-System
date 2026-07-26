package com.smita.dairy.milkentry;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MilkEntryRepository
        extends JpaRepository<MilkEntry, Long> {

    boolean existsByFarmerIdAndCollectionDateAndCollectionShiftAndMilkType(
            Long farmerId,
            LocalDate collectionDate,
            CollectionShift collectionShift,
            com.smita.dairy.ratecard.MilkType milkType
    );

    List<MilkEntry> findByFarmerIdOrderByCollectionDateDesc(
            Long farmerId
    );

    List<MilkEntry> findByCollectionDateBetweenOrderByCollectionDateDesc(
            LocalDate from,
            LocalDate to
    );
}