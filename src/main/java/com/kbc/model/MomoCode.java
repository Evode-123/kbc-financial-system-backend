package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "momo_code")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MomoCode {
     @Id
    @GeneratedValue
    private UUID id;
    private Integer momoCode;
    private String status = "ACTIVE";
}
