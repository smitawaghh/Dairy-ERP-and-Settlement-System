package com.smita.dairy.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DashboardSummaryResponse {

    private LocalDate date;

    private long totalFarmers;
    private long activeFarmers;

    private long milkEntryCount;

    private BigDecimal milkQuantity;
    private BigDecimal milkValue;

    private BigDecimal credits;
    private BigDecimal debits;

    private BigDecimal dailyNetAmount;
    private BigDecimal overallLedgerBalance;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getTotalFarmers() {
        return totalFarmers;
    }

    public void setTotalFarmers(long totalFarmers) {
        this.totalFarmers = totalFarmers;
    }

    public long getActiveFarmers() {
        return activeFarmers;
    }

    public void setActiveFarmers(long activeFarmers) {
        this.activeFarmers = activeFarmers;
    }

    public long getMilkEntryCount() {
        return milkEntryCount;
    }

    public void setMilkEntryCount(long milkEntryCount) {
        this.milkEntryCount = milkEntryCount;
    }

    public BigDecimal getMilkQuantity() {
        return milkQuantity;
    }

    public void setMilkQuantity(BigDecimal milkQuantity) {
        this.milkQuantity = milkQuantity;
    }

    public BigDecimal getMilkValue() {
        return milkValue;
    }

    public void setMilkValue(BigDecimal milkValue) {
        this.milkValue = milkValue;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal credits) {
        this.credits = credits;
    }

    public BigDecimal getDebits() {
        return debits;
    }

    public void setDebits(BigDecimal debits) {
        this.debits = debits;
    }

    public BigDecimal getDailyNetAmount() {
        return dailyNetAmount;
    }

    public void setDailyNetAmount(BigDecimal dailyNetAmount) {
        this.dailyNetAmount = dailyNetAmount;
    }

    public BigDecimal getOverallLedgerBalance() {
        return overallLedgerBalance;
    }

    public void setOverallLedgerBalance(
            BigDecimal overallLedgerBalance) {
        this.overallLedgerBalance = overallLedgerBalance;
    }
}