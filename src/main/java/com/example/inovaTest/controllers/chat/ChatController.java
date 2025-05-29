package com.example.inovaTest.controllers.chat;

import com.example.inovaTest.dtos.chat.*;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/create")
    public ResponseEntity<ChatDTO> createChat(@RequestBody CreateChatRequest request, Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        ChatDTO chat = chatService.createChat(request, currentUser);
        return ResponseEntity.ok(chat);
    }

    @PostMapping("/direct/{userId}")
    public ResponseEntity<ChatDTO> getOrCreateDirectChat(@PathVariable UUID userId, Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        ChatDTO chat = chatService.getOrCreateDirectChat(userId, currentUser);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ChatDTO>> getUserChats(Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        List<ChatDTO> chats = chatService.getUserChats(currentUser);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageDTO>> getChatMessages(
            @PathVariable UUID chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        List<MessageDTO> messages = chatService.getChatMessages(chatId, currentUser, page, size);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{chatId}/read")
    public ResponseEntity<Void> markMessagesAsRead(@PathVariable UUID chatId, Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        chatService.markMessagesAsRead(chatId, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{chatId}/unread-count")
    public ResponseEntity<Long> getUnreadCount(@PathVariable UUID chatId, Authentication auth) {
        UserModel currentUser = (UserModel) auth.getPrincipal();
        Long count = chatService.getUnreadCount(chatId, currentUser);
        return ResponseEntity.ok(count);
    }
}