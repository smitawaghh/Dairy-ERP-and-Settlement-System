package com.smita.dairy.ratecard.dto;

import com.smita.dairy.ratecard.MilkType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RateCardResponse {

    private Long id;
    private MilkType milkType;
    private BigDecimal fatRate;
    private BigDecimal snfRate;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private String remarks;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}