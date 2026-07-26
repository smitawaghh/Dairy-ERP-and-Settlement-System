package com.smita.dairy.farmer.mapper;

import com.smita.dairy.farmer.dto.FarmerRequest;
import com.smita.dairy.farmer.dto.FarmerResponse;
import com.smita.dairy.farmer.Farmer;
import org.springframework.stereotype.Component;

@Component
public class FarmerMapper {

    public Farmer toEntity(FarmerRequest request) {
        Farmer farmer = new Farmer();

        farmer.setFarmerCode(request.getFarmerCode());
        farmer.setFullName(request.getFullName());
        farmer.setMobile(request.getMobile());
        farmer.setVillage(request.getVillage());
        farmer.setAddress(request.getAddress());
        farmer.setBankAccountNumber(request.getBankAccountNumber());
        farmer.setIfsc(request.getIfsc());
        farmer.setAadhaar(request.getAadhaar());
        farmer.setRemarks(request.getRemarks());

        return farmer;
    }

    public FarmerResponse toResponse(Farmer farmer) {
        FarmerResponse response = new FarmerResponse();

        response.setId(farmer.getId());
        response.setFarmerCode(farmer.getFarmerCode());
        response.setFullName(farmer.getFullName());
        response.setMobile(farmer.getMobile());
        response.setVillage(farmer.getVillage());
        response.setAddress(farmer.getAddress());
        response.setActive(farmer.getActive());
        response.setRegisteredAt(farmer.getRegisteredAt());

        return response;
    }

    public void updateEntity(FarmerRequest request, Farmer farmer) {
        farmer.setFullName(request.getFullName());
        farmer.setMobile(request.getMobile());
        farmer.setVillage(request.getVillage());
        farmer.setAddress(request.getAddress());
        farmer.setBankAccountNumber(request.getBankAccountNumber());
        farmer.setIfsc(request.getIfsc());
        farmer.setAadhaar(request.getAadhaar());
        farmer.setRemarks(request.getRemarks());
    }
}