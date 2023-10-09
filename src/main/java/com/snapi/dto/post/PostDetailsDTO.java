package com.snapi.dto.post;

import com.snapi.domain.Post;

import java.time.LocalDateTime;

public record PostDetailsDTO(
        Long id,
        Boolean deleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String title,
        String description,
        String picLink,
        String vidLink,
        Boolean isPrivate,
        Long userId,
        String name,
        String username,
        String userPicLink,
        int likesCount
) {
    public PostDetailsDTO(Post post) {
        this(post.getId(),
                post.getDeleted(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getTitle(),
                post.getDescription(),
                post.getPicLink(),
                post.getVidLink(),
                post.getIsPrivate(),
                post.getUser().getId(),
                post.getUser().getName(),
                post.getUser().getUsername(),
                post.getUser().getPicLink(),
                post.getLikesCount());
    }
}
