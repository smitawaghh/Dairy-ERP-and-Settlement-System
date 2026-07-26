package com.smita.dairy.milkentry;

import com.smita.dairy.farmer.Farmer;
import com.smita.dairy.ratecard.MilkType;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "milk_entries",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_milk_entry_farmer_date_shift_type",
                        columnNames = {
                                "farmer_id",
                                "collection_date",
                                "collection_shift",
                                "milk_type"
                        }
                )
        }
)
public class MilkEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmer_id", nullable = false)
    private Farmer farmer;

    @Column(name = "collection_date", nullable = false)
    private LocalDate collectionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_shift", nullable = false, length = 20)
    private CollectionShift collectionShift;

    @Enumerated(EnumType.STRING)
    @Column(name = "milk_type", nullable = false, length = 20)
    private MilkType milkType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal fat;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal snf;

    @Column(name = "fat_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal fatRate;

    @Column(name = "snf_rate", nullable = false, precision = 10, scale = 4)
    private BigDecimal snfRate;

    @Column(name = "rate_per_litre", nullable = false, precision = 12, scale = 4)
    private BigDecimal ratePerLitre;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(length = 500)
    private String remarks;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
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
}