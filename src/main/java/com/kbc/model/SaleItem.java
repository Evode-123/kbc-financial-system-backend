package com.kbc.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Books book;
    
    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;
    
    private Integer quantity;
    private float unitPrice;
    
}
