package com.example.inovaTest.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatParticipantDTO {
    private UUID id;
    private UUID userId;
    private String username;
    private String profilePicture;
    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;
    private boolean isActive;
}