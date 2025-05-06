package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "cash_offering_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CashOfferingDetails {
     @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "cash_offering_id")
    private CashOffering cashOffering;
    
    private Integer denomination;
    
    private Integer quantity;
    private float total;
}
