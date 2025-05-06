package com.kbc.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleItemDto {
    private Long bookId;
    private Integer quantity;
}