package com.example.inovaTest.dtos.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketMessage {
    private String type; // NEW_MESSAGE, USER_TYPING, MESSAGE_READ, etc.
    private Object payload;
    private String chatId;
    private String senderId;
}