package com.smita.dairy.feedsale;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedSaleRepository
        extends JpaRepository<FeedSale, Long> {

    List<FeedSale> findByFarmerIdOrderBySaleDateDesc(Long farmerId);
}