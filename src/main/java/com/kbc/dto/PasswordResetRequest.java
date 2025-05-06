package com.kbc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {
    @NotBlank(message = "Token cannot be blank")
    private String token;
    
    @NotBlank(message = "New password cannot be blank")
    private String newPassword;
}