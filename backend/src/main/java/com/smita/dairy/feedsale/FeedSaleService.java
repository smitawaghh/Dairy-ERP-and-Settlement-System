package com.smita.dairy.feedsale;

import com.smita.dairy.feedsale.dto.FeedSaleRequest;
import com.smita.dairy.feedsale.dto.FeedSaleResponse;

import java.util.List;

public interface FeedSaleService {

    FeedSaleResponse createFeedSale(FeedSaleRequest request);

    FeedSaleResponse getFeedSaleById(Long id);

    List<FeedSaleResponse> getAllFeedSales();

    List<FeedSaleResponse> getFeedSalesByFarmer(Long farmerId);
}