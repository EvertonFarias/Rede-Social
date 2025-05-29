package com.example.inovaTest.repositories.chat;

import com.example.inovaTest.models.chat.ChatModel;
import com.example.inovaTest.models.chat.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
    
    @Query("SELECT m FROM MessageModel m " +
           "WHERE m.chat = :chat AND m.isDeleted = false " +
           "ORDER BY m.sentAt DESC")
    Page<MessageModel> findByChatOrderBySentAtDesc(@Param("chat") ChatModel chat, Pageable pageable);
    
    @Query("SELECT m FROM MessageModel m " +
           "WHERE m.chat = :chat AND m.isDeleted = false " +
           "ORDER BY m.sentAt DESC")
    List<MessageModel> findTop50ByChatOrderBySentAtDesc(@Param("chat") ChatModel chat);
    
    @Query("SELECT m FROM MessageModel m " +
           "WHERE m.chat = :chat AND m.isDeleted = false " +
           "ORDER BY m.sentAt DESC")
    List<MessageModel> findRecentMessagesByChat(@Param("chat") ChatModel chat, Pageable pageable);
}