package com.kbc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "momo_offering")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoOffering {
    @Id
    @GeneratedValue
    private UUID id;
    private LocalDateTime date;
    
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Services services;
    
    @ManyToOne
    @JoinColumn(name = "offering_type_id")
    private OfferingTypeMomo offeringType;
    
    @ManyToOne
    @JoinColumn(name = "momo_code_id")
    private MomoCode momoCode;
    
    private float amount;
    
    @Column(name = "momo_tithe")
    private Float momoTithe;  // Using Float wrapper to allow null
    
    @PrePersist
    @PreUpdate
    private void calculateTithe() {
        this.momoTithe = this.amount * 0.1f;  // Calculate 10% tithe
    }
    
    // Custom setter to ensure tithe is calculated when amount is set
    public void setAmount(float amount) {
        this.amount = amount;
        calculateTithe();
    }
}