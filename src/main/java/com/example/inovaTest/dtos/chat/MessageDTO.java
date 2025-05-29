package com.example.inovaTest.dtos.chat;

import com.example.inovaTest.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String senderUsername;
    private String senderProfilePicture;
    private String content;
    private MessageType type;
    private LocalDateTime sentAt;
    private LocalDateTime editedAt;
    private boolean isDeleted;
}