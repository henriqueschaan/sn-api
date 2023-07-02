package com.snapi.dto.user;

import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        
        String name,
        String username,
        String phone,
        
        @Email
        String email,
        
        String picLink
        
) {
}
