package com.example.inovaTest.services.websocket;


import com.example.inovaTest.dtos.notifications.NotificationDTO;
import com.example.inovaTest.models.NotificationModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationWebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationModel notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setRecipientId(notification.getRecipient().getId());
        dto.setSenderId(notification.getSender().getId());
        dto.setSenderName(notification.getSender().getUsername()); 
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setReferenceId(notification.getReferenceId());
        dto.setIsRead(notification.getIsRead());
        dto.setCreatedAt(notification.getCreatedAt().toString());

        messagingTemplate.convertAndSendToUser(
                notification.getRecipient().getId().toString(),
                "/queue/notifications",
                dto
        );
    }
}
