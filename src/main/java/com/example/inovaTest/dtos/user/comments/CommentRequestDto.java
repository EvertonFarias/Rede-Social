package com.example.inovaTest.dtos.user.comments;

import java.util.UUID;

import lombok.Data;

@Data
public class CommentRequestDto {
    private UUID userId;
    private String content;

}
