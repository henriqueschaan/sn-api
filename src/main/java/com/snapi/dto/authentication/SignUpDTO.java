package com.snapi.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDTO(
        
        @NotBlank
        String name,
        
        @NotBlank
        String username,
        
        String phone,
        
        @NotBlank
        @Email
        String email,
        
        @NotBlank
        String password,
        
        String picLink
        
) {
}
