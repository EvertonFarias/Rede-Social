package com.example.inovaTest.controllers;

import com.example.inovaTest.dtos.notifications.NotificationDTO;
import com.example.inovaTest.enums.NotificationType;
import com.example.inovaTest.models.NotificationModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.repositories.NotificationRepository;
import com.example.inovaTest.services.NotificationService;
import com.example.inovaTest.services.websocket.NotificationWebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationWebSocketService webSocketService;

    // Buscar todas as notificações de um usuário
    @GetMapping("/user/get-all/{userId}")
    public ResponseEntity<List<NotificationDTO>> getAllByUser(@PathVariable UUID userId) {
        try {
            List<NotificationDTO> notifications = notificationService.getAllByRecipientId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Marcar uma notificação específica como lida
    @PutMapping("/mark-as-read/{notificationId}")
    public ResponseEntity<Void> markAsRead(@PathVariable UUID notificationId) {
        try {
            NotificationModel notification = notificationRepository.findById(notificationId)
                    .orElse(null);

            if (notification == null) {
                return ResponseEntity.notFound().build();
            }

            notification.setRead(true);
            notificationRepository.save(notification);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Marcar todas as notificações de um usuário como lidas
    @PutMapping("/mark-all-as-read/{userId}")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        try {
            List<NotificationModel> notifications = notificationRepository.findByRecipientIdAndIsReadFalse(userId);
            
            for (NotificationModel notification : notifications) {
                notification.setRead(true);
            }
            
            notificationRepository.saveAll(notifications);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Contar notificações não lidas
    @GetMapping("/user/unread-count/{userId}")
    public ResponseEntity<Long> getUnreadCount(@PathVariable UUID userId) {
        try {
            long count = notificationRepository.countByRecipientIdAndIsReadFalse(userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Deletar uma única notificação
    @DeleteMapping("/user/{userId}/{notificationId}")
    public ResponseEntity<Void> deleteNotificationById(
            @PathVariable UUID notificationId,
            @PathVariable UUID userId) {
        try {
            NotificationModel notification = notificationRepository.findById(notificationId)
                    .orElse(null);

            if (notification == null || !notification.getRecipient().getId().equals(userId)) {
                return ResponseEntity.notFound().build();
            }

            notificationRepository.delete(notification);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Deletar todas as notificações de um usuário
    @DeleteMapping("/user/delete-all/{userId}")
    public ResponseEntity<Void> deleteAllByUser(@PathVariable UUID userId) {
        try {
            notificationService.deleteAllByRecipientId(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Enviar notificação (para testes ou uso interno)
    @PostMapping("/send")
    public ResponseEntity<Void> sendNotification(
            @RequestParam UUID recipientId,
            @RequestParam UUID senderId,
            @RequestParam NotificationType type,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) UUID referenceId) {
        try {
            UserModel recipient = userRepository.findById(recipientId)
                    .orElseThrow(() -> new RuntimeException("Recipient not found"));
            UserModel sender = userRepository.findById(senderId)
                    .orElseThrow(() -> new RuntimeException("Sender not found"));

            NotificationModel notification = new NotificationModel(
                    recipient, 
                    sender, 
                    type, 
                    title, 
                    message, 
                    referenceId
            );
            notificationRepository.save(notification);

            webSocketService.sendNotification(notification);
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Buscar notificações por tipo
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByType(
            @PathVariable UUID userId, 
            @PathVariable NotificationType type) {
        try {
            List<NotificationDTO> notifications = notificationService.getByRecipientIdAndType(userId, type);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Buscar apenas notificações não lidas
    @GetMapping("/user/unread/{userId}")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications(@PathVariable UUID userId) {
        try {
            List<NotificationDTO> notifications = notificationService.getUnreadByRecipientId(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}