package com.smita.dairy.milkentry.dto;

import com.smita.dairy.milkentry.CollectionShift;
import com.smita.dairy.ratecard.MilkType;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MilkEntryRequest {

    @NotNull(message = "Farmer id is required")
    private Long farmerId;

    @NotNull(message = "Collection date is required")
    private LocalDate collectionDate;

    @NotNull(message = "Collection shift is required")
    private CollectionShift collectionShift;

    @NotNull(message = "Milk type is required")
    private MilkType milkType;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private BigDecimal quantity;

    @NotNull(message = "FAT is required")
    @DecimalMin(value = "0.0", message = "FAT cannot be negative")
    @DecimalMax(value = "15.0", message = "FAT cannot exceed 15")
    private BigDecimal fat;

    @NotNull(message = "SNF is required")
    @DecimalMin(value = "0.0", message = "SNF cannot be negative")
    @DecimalMax(value = "15.0", message = "SNF cannot exceed 15")
    private BigDecimal snf;

    @Size(max = 500, message = "Remarks cannot exceed 500 characters")
    private String remarks;

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public LocalDate getCollectionDate() {
        return collectionDate;
    }

    public void setCollectionDate(LocalDate collectionDate) {
        this.collectionDate = collectionDate;
    }

    public CollectionShift getCollectionShift() {
        return collectionShift;
    }

    public void setCollectionShift(CollectionShift collectionShift) {
        this.collectionShift = collectionShift;
    }

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getFat() {
        return fat;
    }

    public void setFat(BigDecimal fat) {
        this.fat = fat;
    }

    public BigDecimal getSnf() {
        return snf;
    }

    public void setSnf(BigDecimal snf) {
        this.snf = snf;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}