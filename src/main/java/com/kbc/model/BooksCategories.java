package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books_categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksCategories {
    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    private Category category; 
    
    public enum Category {  
        CLASS1, CLASS2, CLASS3A, CLASS3B, CLASS4A, CLASS4B, CLASS5, CLASS6, CLASS7
    }
}
