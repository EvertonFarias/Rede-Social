package com.example.inovaTest.dtos.notifications;

import com.example.inovaTest.enums.NotificationType;
import lombok.Data;

import java.util.UUID;

@Data
public class NotificationDTO {
    private UUID id;
    private UUID recipientId;
    private UUID senderId;
    private String senderName;
    private String title;
    private String message;
    private NotificationType type;
    private UUID referenceId;
    private Boolean isRead;
    private String createdAt;
}
