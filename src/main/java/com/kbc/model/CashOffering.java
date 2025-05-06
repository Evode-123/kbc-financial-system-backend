package com.kbc.model;

import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cash_offering")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashOffering {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;
    
    @ManyToOne
    @JoinColumn(name = "offering_type_id")
    private OfferingTypeAccount offeringType;
    
    @ManyToOne
    @JoinColumn(name = "account_no_id")
    private AccountNo accountNo;
    
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    
    private float totalAmount;
    
    @Column(name = "cash_tithe")
    private Float cashTithe;  // Using Float wrapper to allow null
    
    @PrePersist
    @PreUpdate
    private void calculateTithe() {
        this.cashTithe = this.totalAmount * 0.1f;  // Calculate 10% tithe
    }
    
    // Custom setter to ensure tithe is calculated when amount is set
    public void setAmount(float amount) {
        this.totalAmount= amount;
        calculateTithe();
    }
}