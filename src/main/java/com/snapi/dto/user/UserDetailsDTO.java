package com.snapi.dto.user;

import com.snapi.domain.User;

public record UserDetailsDTO(
        Long id,
        String name,
        String username,
        String phone,
        String picLink,
        String description
) {
    public UserDetailsDTO(User user) {
        this(user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPhone(),
                user.getPicLink(),
                user.getDescription());
    }
}
