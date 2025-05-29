package com.example.inovaTest.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingIndicator {
    private UUID chatId;
    private UUID userId;
    private String username;
    private boolean isTyping;
}