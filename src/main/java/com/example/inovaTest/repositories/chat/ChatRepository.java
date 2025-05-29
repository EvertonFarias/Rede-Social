package com.example.inovaTest.repositories.chat;

import com.example.inovaTest.models.chat.ChatModel;
import com.example.inovaTest.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatModel, UUID> {
    
    @Query("SELECT DISTINCT c FROM ChatModel c " +
           "JOIN c.participants p " +
           "WHERE p.user = :user AND p.isActive = true " +
           "ORDER BY c.updatedAt DESC")
    List<ChatModel> findChatsByUser(@Param("user") UserModel user);

    @Query("SELECT c FROM ChatModel c " +
           "JOIN c.participants p1 " +
           "JOIN c.participants p2 " +
           "WHERE c.isGroup = false " +
           "AND p1.user = :user1 AND p1.isActive = true " +
           "AND p2.user = :user2 AND p2.isActive = true")
    Optional<ChatModel> findDirectChatBetweenUsers(@Param("user1") UserModel user1, 
                                                   @Param("user2") UserModel user2);
    
    @Query("SELECT COUNT(p) FROM ChatParticipantModel p " +
           "WHERE p.chat = :chat AND p.isActive = true")
    Long countActiveParticipants(@Param("chat") ChatModel chat);
}