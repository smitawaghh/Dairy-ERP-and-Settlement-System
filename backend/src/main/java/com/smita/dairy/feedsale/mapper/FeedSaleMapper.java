package com.smita.dairy.feedsale.mapper;

import com.smita.dairy.feedsale.FeedSale;
import com.smita.dairy.feedsale.dto.FeedSaleRequest;
import com.smita.dairy.feedsale.dto.FeedSaleResponse;

import org.springframework.stereotype.Component;

@Component
public class FeedSaleMapper {

    public FeedSale toEntity(FeedSaleRequest request) {

        FeedSale feedSale = new FeedSale();

        feedSale.setSaleDate(request.getSaleDate());
        feedSale.setFeedName(request.getFeedName());
        feedSale.setQuantity(request.getQuantity());
        feedSale.setUnitPrice(request.getUnitPrice());
        feedSale.setRemarks(request.getRemarks());

        return feedSale;
    }

    public FeedSaleResponse toResponse(FeedSale feedSale) {

        FeedSaleResponse response = new FeedSaleResponse();

        response.setId(feedSale.getId());

        response.setFarmerId(feedSale.getFarmer().getId());
        response.setFarmerCode(feedSale.getFarmer().getFarmerCode());
        response.setFarmerName(feedSale.getFarmer().getFullName());

        response.setSaleDate(feedSale.getSaleDate());
        response.setFeedName(feedSale.getFeedName());
        response.setQuantity(feedSale.getQuantity());
        response.setUnitPrice(feedSale.getUnitPrice());
        response.setTotalAmount(feedSale.getTotalAmount());
        response.setRemarks(feedSale.getRemarks());
        response.setCreatedAt(feedSale.getCreatedAt());

        return response;
    }
}