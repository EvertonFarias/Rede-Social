package com.example.inovaTest.controllers;

import com.example.inovaTest.enums.NotificationType;
import com.example.inovaTest.models.NotificationModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.repositories.NotificationRepository;
import com.example.inovaTest.services.websocket.NotificationWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationWebSocketService webSocketService;

    @PostMapping("/send")
    public void sendNotification(
            @RequestParam UUID recipientId,
            @RequestParam UUID senderId,
            @RequestParam NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) UUID referenceId
    ) {
        UserModel recipient = userRepository.findById(recipientId).orElseThrow();
        UserModel sender = userRepository.findById(senderId).orElseThrow();

        NotificationModel notification = new NotificationModel(recipient, sender, type, title, message, referenceId);
        notificationRepository.save(notification);

        webSocketService.sendNotification(notification);
    }
}
