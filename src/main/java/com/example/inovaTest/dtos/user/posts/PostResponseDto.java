package com.example.inovaTest.dtos.user.posts;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    private long likesCount;
    @JsonProperty("isLiked") // pra forçar o nome do campo no JSON
    private boolean isLiked;
    private long commentsCount;

}
