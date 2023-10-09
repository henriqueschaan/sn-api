package com.snapi.dto.user;

import com.snapi.domain.User;
import jakarta.validation.constraints.Email;

public record UserUpdateDTO(
        String name,
        String username,
        String phone,
        @Email
        String email,
        String password,
        String picLink,
        String description

) {
    public UserUpdateDTO(User user) {
        this(user.getName(),
                user.getUsername(),
                user.getPhone(),
                user.getEmail(),
                user.getPassword(),
                user.getPicLink(),
                user.getDescription());
    }
}