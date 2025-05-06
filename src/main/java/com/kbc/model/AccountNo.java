package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "account_no")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountNo {
    @Id
    @GeneratedValue
    private UUID id;
    private long accountNo;
    
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private String status = "ACTIVE";
}
