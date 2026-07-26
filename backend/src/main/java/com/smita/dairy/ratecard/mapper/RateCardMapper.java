package com.smita.dairy.ratecard.mapper;

import com.smita.dairy.ratecard.RateCard;
import com.smita.dairy.ratecard.dto.RateCardRequest;
import com.smita.dairy.ratecard.dto.RateCardResponse;
import org.springframework.stereotype.Component;

@Component
public class RateCardMapper {

    public RateCard toEntity(RateCardRequest request) {

        RateCard rateCard = new RateCard();

        rateCard.setMilkType(request.getMilkType());
        rateCard.setFatRate(request.getFatRate());
        rateCard.setSnfRate(request.getSnfRate());
        rateCard.setEffectiveFrom(request.getEffectiveFrom());
        rateCard.setEffectiveTo(request.getEffectiveTo());
        rateCard.setRemarks(request.getRemarks());

        return rateCard;
    }

    public RateCardResponse toResponse(RateCard rateCard) {

        RateCardResponse response = new RateCardResponse();

        response.setId(rateCard.getId());
        response.setMilkType(rateCard.getMilkType());
        response.setFatRate(rateCard.getFatRate());
        response.setSnfRate(rateCard.getSnfRate());
        response.setEffectiveFrom(rateCard.getEffectiveFrom());
        response.setEffectiveTo(rateCard.getEffectiveTo());
        response.setRemarks(rateCard.getRemarks());
        response.setCreatedAt(rateCard.getCreatedAt());

        return response;
    }
}