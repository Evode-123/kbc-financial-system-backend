package com.kbc.dto;

import com.kbc.model.ERole;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean passwordChanged;
    private Set<ERole> roles;
}