package com.snapi.dto.post;

public record PostUpdateDTO(
        String title,
        String description,
        String picLink,
        String vidLink,
        Boolean isPrivate
) {
}
