package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offeringtype_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferingTypeAccount {
    @Id
    @GeneratedValue
    private UUID id;
    private String offeringTypeName;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountNo accountNo;
    
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
}
