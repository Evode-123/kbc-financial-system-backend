package com.kbc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expenses {
    @Id
    @GeneratedValue
    private UUID id;
    
    private LocalDate date;
    private String chequeNo;
    private double openingBalance;
    private double amountWithdrawn;
    
    @ManyToOne
    @JoinColumn(name = "account_no_id")
    private AccountNo accountNo;
    
    @OneToMany(mappedBy = "expenses", cascade = CascadeType.ALL)
    @JsonIgnore  // Add this annotation
    private List<ExpensesDetails> expensesDetails;
    
    private double totalAmount;
    private double closingBalance;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}