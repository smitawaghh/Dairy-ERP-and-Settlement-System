package com.smita.dairy.milkentry;

import com.smita.dairy.milkentry.dto.MilkEntryRequest;
import com.smita.dairy.milkentry.dto.MilkEntryResponse;

import java.time.LocalDate;
import java.util.List;

public interface MilkEntryService {

    MilkEntryResponse createMilkEntry(MilkEntryRequest request);

    MilkEntryResponse getMilkEntryById(Long id);

    List<MilkEntryResponse> getAllMilkEntries();

    List<MilkEntryResponse> getMilkEntriesByFarmer(Long farmerId);

    List<MilkEntryResponse> getMilkEntriesByDateRange(
            LocalDate from,
            LocalDate to
    );
}