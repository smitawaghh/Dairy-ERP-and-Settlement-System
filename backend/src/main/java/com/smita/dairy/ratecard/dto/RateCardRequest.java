package com.smita.dairy.ratecard.dto;

import com.smita.dairy.ratecard.MilkType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RateCardRequest {

    @NotNull(message = "Milk type is required")
    private MilkType milkType;

    @NotNull(message = "FAT rate is required")
    @DecimalMin(value = "0.0", message = "FAT rate cannot be negative")
    private BigDecimal fatRate;

    @NotNull(message = "SNF rate is required")
    @DecimalMin(value = "0.0", message = "SNF rate cannot be negative")
    private BigDecimal snfRate;

    @NotNull(message = "Effective from date is required")
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;
    private String remarks;

    public MilkType getMilkType() {
        return milkType;
    }

    public void setMilkType(MilkType milkType) {
        this.milkType = milkType;
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

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}