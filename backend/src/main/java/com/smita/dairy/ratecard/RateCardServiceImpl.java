package com.smita.dairy.ratecard;

import com.smita.dairy.ratecard.dto.RateCardRequest;
import com.smita.dairy.ratecard.dto.RateCardResponse;
import com.smita.dairy.ratecard.exception.InvalidRateCardException;
import com.smita.dairy.ratecard.exception.RateCardNotFoundException;
import com.smita.dairy.ratecard.mapper.RateCardMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class RateCardServiceImpl implements RateCardService {

    private final RateCardRepository rateCardRepository;
    private final RateCardMapper rateCardMapper;

    public RateCardServiceImpl(
            RateCardRepository rateCardRepository,
            RateCardMapper rateCardMapper) {

        this.rateCardRepository = rateCardRepository;
        this.rateCardMapper = rateCardMapper;
    }

    @Override
    public RateCardResponse createRateCard(RateCardRequest request) {

        validateDates(request);

        long overlapCount;

        if (request.getEffectiveTo() == null) {

            overlapCount =
                    rateCardRepository.countOverlappingOpenEndedRateCards(
                            request.getMilkType(),
                            request.getEffectiveFrom()
                    );

        } else {

            overlapCount =
                    rateCardRepository.countOverlappingRateCardsWithEndDate(
                            request.getMilkType(),
                            request.getEffectiveFrom(),
                            request.getEffectiveTo()
                    );
        }

        if (overlapCount > 0) {
            throw new InvalidRateCardException(
                    "Rate card overlaps with an existing rate card for "
                            + request.getMilkType()
            );
        }

        RateCard rateCard =
                rateCardMapper.toEntity(request);

        RateCard savedRateCard =
                rateCardRepository.save(rateCard);

        return rateCardMapper.toResponse(savedRateCard);
    }

    @Override
    @Transactional(readOnly = true)
    public RateCardResponse getRateCardById(Long id) {

        RateCard rateCard =
                rateCardRepository.findById(id)
                        .orElseThrow(() ->
                                new RateCardNotFoundException(
                                        "Rate card not found with id: " + id
                                )
                        );

        return rateCardMapper.toResponse(rateCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RateCardResponse> getAllRateCards() {

        return rateCardRepository.findAll()
                .stream()
                .map(rateCardMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RateCardResponse getApplicableRateCard(
            MilkType milkType,
            LocalDate date) {

        RateCard rateCard =
                rateCardRepository
                        .findApplicableRateCard(milkType, date)
                        .orElseThrow(() ->
                                new RateCardNotFoundException(
                                        "No applicable rate card found for "
                                                + milkType
                                                + " on "
                                                + date
                                )
                        );

        return rateCardMapper.toResponse(rateCard);
    }

    private void validateDates(RateCardRequest request) {

        if (request.getEffectiveFrom() == null) {
            throw new InvalidRateCardException(
                    "Effective from date is required"
            );
        }

        if (request.getEffectiveTo() != null
                && request.getEffectiveTo()
                .isBefore(request.getEffectiveFrom())) {

            throw new InvalidRateCardException(
                    "Effective to date cannot be before effective from date"
            );
        }
    }
}