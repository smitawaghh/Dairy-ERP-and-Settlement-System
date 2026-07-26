package com.smita.dairy.milkentry.mapper;

import com.smita.dairy.milkentry.MilkEntry;
import com.smita.dairy.milkentry.dto.MilkEntryRequest;
import com.smita.dairy.milkentry.dto.MilkEntryResponse;

import org.springframework.stereotype.Component;

@Component
public class MilkEntryMapper {

    public MilkEntry toEntity(MilkEntryRequest request) {

        MilkEntry entry = new MilkEntry();

        entry.setCollectionDate(request.getCollectionDate());
        entry.setCollectionShift(request.getCollectionShift());
        entry.setMilkType(request.getMilkType());
        entry.setQuantity(request.getQuantity());
        entry.setFat(request.getFat());
        entry.setSnf(request.getSnf());
        entry.setRemarks(request.getRemarks());

        return entry;
    }

    public MilkEntryResponse toResponse(MilkEntry entry) {

        MilkEntryResponse response = new MilkEntryResponse();

        response.setId(entry.getId());

        response.setFarmerId(entry.getFarmer().getId());
        response.setFarmerCode(entry.getFarmer().getFarmerCode());
        response.setFarmerName(entry.getFarmer().getFullName());

        response.setCollectionDate(entry.getCollectionDate());
        response.setCollectionShift(entry.getCollectionShift());
        response.setMilkType(entry.getMilkType());

        response.setQuantity(entry.getQuantity());
        response.setFat(entry.getFat());
        response.setSnf(entry.getSnf());

        response.setFatRate(entry.getFatRate());
        response.setSnfRate(entry.getSnfRate());
        response.setRatePerLitre(entry.getRatePerLitre());
        response.setAmount(entry.getAmount());

        response.setRemarks(entry.getRemarks());
        response.setCreatedAt(entry.getCreatedAt());

        return response;
    }
}