package com.example.inovaTest.dtos.chat;

import com.example.inovaTest.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private UUID chatId;
    private String content;
    private MessageType type;
}