package com.snapi.dto.user;

import com.snapi.domain.User;

public record UserProfileDTO(
        Long id,
        String name,
        String username,
        String phone,
        String picLink,
        String description,
        int postCount,
        int friendCount,
        boolean isFriend
) {
    public UserProfileDTO(User user, int postCount, int friendCount, boolean isFriend) {
        this(user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPhone(),
                user.getPicLink(),
                user.getDescription(),
                postCount,
                friendCount,
                isFriend);
    }
}