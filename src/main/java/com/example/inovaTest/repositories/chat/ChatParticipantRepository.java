package com.example.inovaTest.repositories.chat;

import com.example.inovaTest.models.chat.ChatModel;
import com.example.inovaTest.models.chat.ChatParticipantModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipantModel, UUID> {
    
    Optional<ChatParticipantModel> findByChatAndUser(ChatModel chat, UserModel user);
    
    List<ChatParticipantModel> findByChatAndIsActiveTrue(ChatModel chat);
    
    @Query("SELECT COUNT(m) FROM MessageModel m " +
           "WHERE m.chat = :chat " +
           "AND m.sentAt > :lastReadAt " +
           "AND m.sender != :user " +
           "AND m.isDeleted = false")
    Long countUnreadMessages(@Param("chat") ChatModel chat, 
                           @Param("lastReadAt") java.time.LocalDateTime lastReadAt, 
                           @Param("user") UserModel user);
}