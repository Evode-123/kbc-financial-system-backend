package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expenses_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpensesDetails {
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "expenses_id")
    private Expenses expenses;
    
    private double amount;
    private String description;
}