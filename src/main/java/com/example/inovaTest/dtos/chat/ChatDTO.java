package com.example.inovaTest.dtos.chat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private UUID id;
    private String name;
    private boolean isGroup;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ChatParticipantDTO> participants;
    private MessageDTO lastMessage;
    private Long unreadCount;
}