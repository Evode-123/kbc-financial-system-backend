package com.kbc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bank_offering")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankOffering {
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
    @JoinColumn(name = "account_id")
    private AccountNo accountNo;
    
    private float amount;
    
    @Column(name = "bank_tithe")
    private Float bankTithe;  // Using Float wrapper to allow null
    
    @PrePersist
    @PreUpdate
    private void calculateTithe() {
        this.bankTithe = this.amount * 0.1f;  // Calculate 10% tithe
    }
    
    // Custom setter to ensure tithe is calculated when amount is set
    public void setAmount(float amount) {
        this.amount = amount;
        calculateTithe();
    }
}