package com.smita.dairy.farmer;

import com.smita.dairy.farmer.dto.FarmerRequest;
import com.smita.dairy.farmer.dto.FarmerResponse;

import java.util.List;

public interface FarmerService {

    FarmerResponse createFarmer(FarmerRequest request);

    FarmerResponse getFarmerById(Long id);

    List<FarmerResponse> getAllFarmers();

    FarmerResponse updateFarmer(Long id, FarmerRequest request);

    void deactivateFarmer(Long id);
}