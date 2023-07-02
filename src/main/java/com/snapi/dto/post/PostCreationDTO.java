package com.snapi.dto.post;

import jakarta.validation.constraints.NotBlank;

public record PostCreationDTO(
        @NotBlank
        String title,
        
        String description,
        String picLink,
        String vidLink
) {
}
