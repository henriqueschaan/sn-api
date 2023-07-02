package com.snapi.dto.user;

import com.snapi.domain.User;

import java.time.LocalDate;

public record UserDetailsDTO(
        Long id,
        LocalDate createdAt,
        LocalDate updatedAt,
        String name,
        String username
) {
    public UserDetailsDTO(User user) {
        this(user.getId(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getName(),
                user.getUsername());
    }
}
