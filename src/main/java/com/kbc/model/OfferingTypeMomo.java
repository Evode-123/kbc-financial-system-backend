package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offeringtype_momo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferingTypeMomo {
    @Id
    @GeneratedValue
    private UUID id;
    private String offeringTypeName;
    
    @ManyToOne
    @JoinColumn(name = "momo_code_id")
    private MomoCode momoCode;
}
