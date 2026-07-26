package com.smita.dairy.ratecard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RateCardRepository extends JpaRepository<RateCard, Long> {

    @Query("""
            SELECT r
            FROM RateCard r
            WHERE r.milkType = :milkType
              AND r.effectiveFrom <= :date
              AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
            ORDER BY r.effectiveFrom DESC
            """)
    List<RateCard> findApplicableRateCards(
            @Param("milkType") MilkType milkType,
            @Param("date") LocalDate date
    );

    default Optional<RateCard> findApplicableRateCard(
            MilkType milkType,
            LocalDate date) {

        return findApplicableRateCards(milkType, date)
                .stream()
                .findFirst();
    }

    @Query("""
            SELECT COUNT(r)
            FROM RateCard r
            WHERE r.milkType = :milkType
              AND r.effectiveFrom <= :effectiveTo
              AND (r.effectiveTo IS NULL OR r.effectiveTo >= :effectiveFrom)
            """)
    long countOverlappingRateCardsWithEndDate(
            @Param("milkType") MilkType milkType,
            @Param("effectiveFrom") LocalDate effectiveFrom,
            @Param("effectiveTo") LocalDate effectiveTo
    );

    @Query("""
            SELECT COUNT(r)
            FROM RateCard r
            WHERE r.milkType = :milkType
              AND (r.effectiveTo IS NULL OR r.effectiveTo >= :effectiveFrom)
            """)
    long countOverlappingOpenEndedRateCards(
            @Param("milkType") MilkType milkType,
            @Param("effectiveFrom") LocalDate effectiveFrom
    );
}