package com.smita.dairy.settlement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SettlementRepository
        extends JpaRepository<Settlement, Long> {

    List<Settlement>
    findByFarmerIdOrderByPeriodFromDesc(Long farmerId);

    boolean existsByFarmerIdAndPeriodFromLessThanEqualAndPeriodToGreaterThanEqual(
            Long farmerId,
            LocalDate periodTo,
            LocalDate periodFrom
    );
}