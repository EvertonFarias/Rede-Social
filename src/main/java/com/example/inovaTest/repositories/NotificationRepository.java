package com.example.inovaTest.repositories;

import com.example.inovaTest.enums.NotificationType;
import com.example.inovaTest.models.NotificationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
    
    // Buscar todas as notificações de um usuário ordenadas por data de criação 
    @Query("SELECT n FROM NotificationModel n WHERE n.recipient.id = :recipientId ORDER BY n.createdAt DESC")
    List<NotificationModel> findByRecipientIdOrderByCreatedAtDesc(@Param("recipientId") UUID recipientId);
    
    // Buscar notificações não lidas de um usuário
    @Query("SELECT n FROM NotificationModel n WHERE n.recipient.id = :recipientId AND n.isRead = false ORDER BY n.createdAt DESC")
    List<NotificationModel> findByRecipientIdAndIsReadFalse(@Param("recipientId") UUID recipientId);
    
    // Contar notificações não lidas de um usuário
    @Query("SELECT COUNT(n) FROM NotificationModel n WHERE n.recipient.id = :recipientId AND n.isRead = false")
    long countByRecipientIdAndIsReadFalse(@Param("recipientId") UUID recipientId);
    
    // Buscar notificações por tipo
    @Query("SELECT n FROM NotificationModel n WHERE n.recipient.id = :recipientId AND n.type = :type ORDER BY n.createdAt DESC")
    List<NotificationModel> findByRecipientIdAndType(@Param("recipientId") UUID recipientId, @Param("type") NotificationType type);
    
    // Deletar todas as notificações de um usuário
    void deleteByRecipientId(UUID recipientId);
    
    // Buscar notificações por remetente
    @Query("SELECT n FROM NotificationModel n WHERE n.sender.id = :senderId ORDER BY n.createdAt DESC")
    List<NotificationModel> findBySenderIdOrderByCreatedAtDesc(@Param("senderId") UUID senderId);
    
    // Buscar notificações por referência (ex: ID do post)
    @Query("SELECT n FROM NotificationModel n WHERE n.referenceId = :referenceId ORDER BY n.createdAt DESC")
    List<NotificationModel> findByReferenceIdOrderByCreatedAtDesc(@Param("referenceId") UUID referenceId);
    
    // Buscar notificações não lidas por tipo
    @Query("SELECT n FROM NotificationModel n WHERE n.recipient.id = :recipientId AND n.type = :type AND n.isRead = false ORDER BY n.createdAt DESC")
    List<NotificationModel> findByRecipientIdAndTypeAndIsReadFalse(@Param("recipientId") UUID recipientId, @Param("type") NotificationType type);
}