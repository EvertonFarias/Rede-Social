package com.example.inovaTest.dtos.user.posts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PostRequestDto {

    @NotNull
    private UUID userId;

    @NotBlank
    private String content;

    private String imageUrl;

    private String videoUrl;
}