package com.kbc.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "book_sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Books {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private BigDecimal price;
    private Integer stockQuantity;
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private BooksCategories category;
}