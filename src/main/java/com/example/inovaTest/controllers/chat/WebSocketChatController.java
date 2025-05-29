package com.example.inovaTest.controllers.chat;

import com.example.inovaTest.dtos.chat.*;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.services.chat.ChatService;
import com.example.inovaTest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class WebSocketChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserRepository userRepository;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload SendMessageRequest request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            UserModel sender = userRepository.getByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            MessageDTO message = chatService.sendMessage(request, sender);

            // Criar WebSocket message
            WebSocketMessage wsMessage = new WebSocketMessage();
            wsMessage.setType("NEW_MESSAGE");
            wsMessage.setPayload(message);
            wsMessage.setChatId(request.getChatId().toString());
            wsMessage.setSenderId(sender.getId().toString());

            // Enviar para todos os participantes do chat
            messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId(), wsMessage);

        } catch (Exception e) {
            System.err.println("Erro ao enviar mensagem: " + e.getMessage());
        }
    }

    @MessageMapping("/chat.typing")
    public void handleTyping(@Payload TypingIndicator typingIndicator, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            UserModel user = userRepository.getByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            typingIndicator.setUserId(user.getId());
            typingIndicator.setUsername(user.getLogin());

            // Criar WebSocket message
            WebSocketMessage wsMessage = new WebSocketMessage();
            wsMessage.setType("USER_TYPING");
            wsMessage.setPayload(typingIndicator);
            wsMessage.setChatId(typingIndicator.getChatId().toString());
            wsMessage.setSenderId(user.getId().toString());

            // Enviar para todos os participantes do chat exceto o remetente
            messagingTemplate.convertAndSend("/topic/chat/" + typingIndicator.getChatId(), wsMessage);

        } catch (Exception e) {
            System.err.println("Erro ao processar typing indicator: " + e.getMessage());
        }
    }

    @MessageMapping("/chat.markAsRead")
    public void markAsRead(@Payload MarkAsReadRequest request, SimpMessageHeaderAccessor headerAccessor) {
        try {
            String username = (String) headerAccessor.getSessionAttributes().get("username");
            UserModel user = userRepository.getByLogin(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            chatService.markMessagesAsRead(request.getChatId(), user);

            // Criar WebSocket message para notificar outros participantes
            WebSocketMessage wsMessage = new WebSocketMessage();
            wsMessage.setType("MESSAGE_READ");
            wsMessage.setPayload(request);
            wsMessage.setChatId(request.getChatId().toString());
            wsMessage.setSenderId(user.getId().toString());

            messagingTemplate.convertAndSend("/topic/chat/" + request.getChatId(), wsMessage);

        } catch (Exception e) {
            System.err.println("Erro ao marcar como lido: " + e.getMessage());
        }
    }
}