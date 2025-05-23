package com.example.inovaTest.dtos.user.posts;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PostResponseDto {
    private UUID id;
    private UUID userId;
    private String username;
    private String content;
    private String imageUrl;
    private String videoUrl;
    private LocalDateTime createdAt;
    private String profilePicture;
}