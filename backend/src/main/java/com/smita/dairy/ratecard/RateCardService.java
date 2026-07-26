package com.smita.dairy.ratecard;

import com.smita.dairy.ratecard.dto.RateCardRequest;
import com.smita.dairy.ratecard.dto.RateCardResponse;

import java.time.LocalDate;
import java.util.List;

public interface RateCardService {

    RateCardResponse createRateCard(RateCardRequest request);

    RateCardResponse getRateCardById(Long id);

    List<RateCardResponse> getAllRateCards();

    RateCardResponse getApplicableRateCard(
            MilkType milkType,
            LocalDate date
    );
}