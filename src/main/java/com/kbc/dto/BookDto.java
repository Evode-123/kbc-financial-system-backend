package com.kbc.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private String title;
    private float price;
    private Integer stockQuantity;
    private Long categoryId;
}
