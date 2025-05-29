package com.example.inovaTest.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChatRequest {
    private String name; // Para chats em grupo
    private boolean isGroup;
    private List<UUID> participantIds;
}
