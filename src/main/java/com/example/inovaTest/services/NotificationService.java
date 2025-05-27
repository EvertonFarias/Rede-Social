package com.example.inovaTest.services;

import com.example.inovaTest.dtos.notifications.NotificationDTO;
import com.example.inovaTest.enums.NotificationType;
import com.example.inovaTest.models.NotificationModel;
import com.example.inovaTest.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    // Buscar todas as notificações de um usuário
    public List<NotificationDTO> getAllByRecipientId(UUID recipientId) {
        List<NotificationModel> notifications = notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(recipientId);
        
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar apenas notificações não lidas
    public List<NotificationDTO> getUnreadByRecipientId(UUID recipientId) {
        List<NotificationModel> notifications = notificationRepository
                .findByRecipientIdAndIsReadFalse(recipientId);
        
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Buscar notificações por tipo
    public List<NotificationDTO> getByRecipientIdAndType(UUID recipientId, NotificationType type) {
        List<NotificationModel> notifications = notificationRepository
                .findByRecipientIdAndType(recipientId, type);
        
        return notifications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    // Contar notificações não lidas
    public long countUnreadByRecipientId(UUID recipientId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(recipientId);
    }
    
    // Marcar uma notificação como lida
    @Transactional
    public void markAsRead(UUID notificationId) {
        NotificationModel notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada"));
        
        notification.setRead(true);
        notificationRepository.save(notification);
    }
    
    // Marcar todas as notificações de um usuário como lidas
    @Transactional
    public void markAllAsReadByRecipientId(UUID recipientId) {
        List<NotificationModel> notifications = notificationRepository
                .findByRecipientIdAndIsReadFalse(recipientId);
        
        notifications.forEach(notification -> notification.setRead(true));
        notificationRepository.saveAll(notifications);
    }
    
    // Deletar uma notificação específica
    @Transactional
    public void deleteById(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }
    
    // Deletar todas as notificações de um usuário
    @Transactional
    public void deleteAllByRecipientId(UUID recipientId) {
        notificationRepository.deleteByRecipientId(recipientId);
    }
    
    // Salvar uma nova notificação
    public NotificationModel save(NotificationModel notification) {
        return notificationRepository.save(notification);
    }

    private NotificationDTO convertToDTO(NotificationModel model) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(model.getId());
        dto.setRecipientId(model.getRecipient().getId());
        dto.setSenderId(model.getSender().getId());
        dto.setSenderName(model.getSender().getUsername());
        dto.setTitle(model.getTitle());
        dto.setMessage(model.getMessage());
        dto.setType(model.getType());
        dto.setReferenceId(model.getReferenceId());
        dto.setIsRead(model.isRead());
        dto.setCreatedAt(model.getCreatedAt().toString());
        dto.setSenderProfilePicture(model.getSender().getProfilePicture());
        return dto;
    }
}
