package com.smita.dairy.statement.dto;

import com.smita.dairy.ledger.dto.LedgerEntryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class StatementResponse {

    private Long farmerId;
    private String farmerCode;
    private String farmerName;

    private LocalDate periodFrom;
    private LocalDate periodTo;

    private BigDecimal totalCredits;
    private BigDecimal totalDebits;
    private BigDecimal netAmount;

    private List<LedgerEntryResponse> transactions;

    public StatementResponse() {
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

    public LocalDate getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
    }

    public BigDecimal getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(BigDecimal totalCredits) {
        this.totalCredits = totalCredits;
    }

    public BigDecimal getTotalDebits() {
        return totalDebits;
    }

    public void setTotalDebits(BigDecimal totalDebits) {
        this.totalDebits = totalDebits;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public List<LedgerEntryResponse> getTransactions() {
        return transactions;
    }

    public void setTransactions(
            List<LedgerEntryResponse> transactions) {
        this.transactions = transactions;
    }
}