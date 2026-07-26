package com.smita.dairy.milkentry.dto;

import com.smita.dairy.milkentry.CollectionShift;
import com.smita.dairy.ratecard.MilkType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MilkEntryResponse {

    private Long id;
    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private LocalDate collectionDate;
    private CollectionShift collectionShift;
    private MilkType milkType;

    private BigDecimal quantity;
    private BigDecimal fat;
    private BigDecimal snf;

    private BigDecimal fatRate;
    private BigDecimal snfRate;
    private BigDecimal ratePerLitre;
    private BigDecimal amount;

    private String remarks;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(Long farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerCode() {
        return farmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        this.farmerCode = farmerCode;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
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

    public BigDecimal getFatRate() {
        return fatRate;
    }

    public void setFatRate(BigDecimal fatRate) {
        this.fatRate = fatRate;
    }

    public BigDecimal getSnfRate() {
        return snfRate;
    }

    public void setSnfRate(BigDecimal snfRate) {
        this.snfRate = snfRate;
    }

    public BigDecimal getRatePerLitre() {
        return ratePerLitre;
    }

    public void setRatePerLitre(BigDecimal ratePerLitre) {
        this.ratePerLitre = ratePerLitre;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}