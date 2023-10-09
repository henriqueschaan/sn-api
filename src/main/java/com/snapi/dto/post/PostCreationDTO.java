package com.snapi.dto.post;

public record PostCreationDTO(
        String title,
        String description,
        String picLink,
        String vidLink
) {
}
