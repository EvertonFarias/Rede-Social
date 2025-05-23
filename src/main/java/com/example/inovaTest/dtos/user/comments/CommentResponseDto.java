package com.example.inovaTest.dtos.user.comments;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CommentResponseDto {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private String profilePicture;
    private String username;
    private UUID userId;

    public CommentResponseDto(UUID id, String content, LocalDateTime createdAt, String profilePicture, String username, UUID userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.profilePicture = profilePicture;
        this.username = username;
        this.userId = userId;
    }
}
