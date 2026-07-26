package com.smita.dairy.farmer;

import com.smita.dairy.farmer.dto.FarmerRequest;
import com.smita.dairy.farmer.dto.FarmerResponse;
import com.smita.dairy.farmer.exception.DuplicateFarmerException;
import com.smita.dairy.farmer.exception.FarmerNotFoundException;
import com.smita.dairy.farmer.mapper.FarmerMapper;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FarmerServiceImpl implements FarmerService {

    private final FarmerRepository farmerRepository;
    private final FarmerMapper farmerMapper;

    public FarmerServiceImpl(
            FarmerRepository farmerRepository,
            FarmerMapper farmerMapper) {

        this.farmerRepository = farmerRepository;
        this.farmerMapper = farmerMapper;
    }

    @Override
    public FarmerResponse createFarmer(FarmerRequest request) {

        if (farmerRepository.existsByFarmerCode(request.getFarmerCode())) {
            throw new DuplicateFarmerException(
                    "Farmer code already exists: " + request.getFarmerCode()
            );
        }

        Farmer farmer = farmerMapper.toEntity(request);

        Farmer savedFarmer = farmerRepository.save(farmer);

        return farmerMapper.toResponse(savedFarmer);
    }

    @Override
    @Transactional(readOnly = true)
    public FarmerResponse getFarmerById(Long id) {

        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException(id));

        return farmerMapper.toResponse(farmer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FarmerResponse> getAllFarmers() {

        return farmerRepository.findAll()
                .stream()
                .map(farmerMapper::toResponse)
                .toList();
    }

    @Override
    public FarmerResponse updateFarmer(
            Long id,
            FarmerRequest request) {

        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException(id));

        farmerMapper.updateEntity(request, farmer);

        Farmer updatedFarmer = farmerRepository.save(farmer);

        return farmerMapper.toResponse(updatedFarmer);
    }

    @Override
    public void deactivateFarmer(Long id) {

        Farmer farmer = farmerRepository.findById(id)
                .orElseThrow(() -> new FarmerNotFoundException(id));

        farmer.setActive(false);

        farmerRepository.save(farmer);
    }
}