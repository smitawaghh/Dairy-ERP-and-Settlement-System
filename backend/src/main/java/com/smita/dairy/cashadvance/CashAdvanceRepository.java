package com.smita.dairy.cashadvance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CashAdvanceRepository
        extends JpaRepository<CashAdvance, Long> {

    List<CashAdvance>
    findByFarmerIdOrderByAdvanceDateDesc(Long farmerId);

    List<CashAdvance>
    findByAdvanceDateBetweenOrderByAdvanceDateDesc(
            LocalDate from,
            LocalDate to
    );
}