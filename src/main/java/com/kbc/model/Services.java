package com.kbc.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Services {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private ServiceName serviceName;

    public enum ServiceName {
        FIRST, SECOND, THURSDAY, OTHERS
    }
}
