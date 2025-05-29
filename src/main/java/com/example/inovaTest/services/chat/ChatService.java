package com.example.inovaTest.services.chat;

import com.example.inovaTest.dtos.chat.*;
import com.example.inovaTest.models.*;
import com.example.inovaTest.models.chat.ChatModel;
import com.example.inovaTest.models.chat.ChatParticipantModel;
import com.example.inovaTest.models.chat.MessageModel;
import com.example.inovaTest.repositories.*;
import com.example.inovaTest.repositories.chat.ChatParticipantRepository;
import com.example.inovaTest.repositories.chat.ChatRepository;
import com.example.inovaTest.repositories.chat.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatParticipantRepository participantRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ChatDTO createChat(CreateChatRequest request, UserModel currentUser) {
        ChatModel chat = new ChatModel();
        chat.setName(request.getName());
        chat.setGroup(request.isGroup());
        
        chat = chatRepository.save(chat);

        // Adicionar o criador do chat
        addParticipantToChat(chat, currentUser);

        // Adicionar outros participantes
        for (UUID participantId : request.getParticipantIds()) {
            if (!participantId.equals(currentUser.getId())) {
                UserModel participant = userRepository.findById(participantId)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                addParticipantToChat(chat, participant);
            }
        }

        return convertToChatDTO(chat, currentUser);
    }

    @Transactional
    public ChatDTO getOrCreateDirectChat(UUID otherUserId, UserModel currentUser) {
        UserModel otherUser = userRepository.findById(otherUserId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se já existe um chat direto entre os usuários
        Optional<ChatModel> existingChat = chatRepository.findDirectChatBetweenUsers(currentUser, otherUser);
        
        if (existingChat.isPresent()) {
            return convertToChatDTO(existingChat.get(), currentUser);
        }

        // Criar novo chat direto
        ChatModel chat = new ChatModel();
        chat.setName(null); // Chats diretos não têm nome
        chat.setGroup(false);
        
        chat = chatRepository.save(chat);

        // Adicionar participantes
        addParticipantToChat(chat, currentUser);
        addParticipantToChat(chat, otherUser);

        return convertToChatDTO(chat, currentUser);
    }

    public List<ChatDTO> getUserChats(UserModel user) {
        List<ChatModel> chats = chatRepository.findChatsByUser(user);
        return chats.stream()
            .map(chat -> convertToChatDTO(chat, user))
            .collect(Collectors.toList());
    }

    public List<MessageDTO> getChatMessages(UUID chatId, UserModel user, int page, int size) {
        ChatModel chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        // Verificar se o usuário é participante do chat
        if (!isUserParticipant(chat, user)) {
            throw new RuntimeException("Usuário não é participante deste chat");
        }

        PageRequest pageRequest = PageRequest.of(page, size);
        List<MessageModel> messages = messageRepository.findRecentMessagesByChat(chat, pageRequest);
        
        return messages.stream()
            .map(this::convertToMessageDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public MessageDTO sendMessage(SendMessageRequest request, UserModel sender) {
        ChatModel chat = chatRepository.findById(request.getChatId())
            .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        if (!isUserParticipant(chat, sender)) {
            throw new RuntimeException("Usuário não é participante deste chat");
        }

        MessageModel message = new MessageModel();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setType(request.getType());

        message = messageRepository.save(message);

        // Atualizar timestamp do chat
        chat.setUpdatedAt(LocalDateTime.now());
        chatRepository.save(chat);

        return convertToMessageDTO(message);
    }

    @Transactional
    public void markMessagesAsRead(UUID chatId, UserModel user) {
        ChatModel chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        ChatParticipantModel participant = participantRepository.findByChatAndUser(chat, user)
            .orElseThrow(() -> new RuntimeException("Usuário não é participante deste chat"));

        participant.setLastReadAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    public Long getUnreadCount(UUID chatId, UserModel user) {
        ChatModel chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new RuntimeException("Chat não encontrado"));

        ChatParticipantModel participant = participantRepository.findByChatAndUser(chat, user)
            .orElseThrow(() -> new RuntimeException("Usuário não é participante deste chat"));

        LocalDateTime lastReadAt = participant.getLastReadAt();
        if (lastReadAt == null) {
            lastReadAt = participant.getJoinedAt();
        }

        return participantRepository.countUnreadMessages(chat, lastReadAt, user);
    }

    private void addParticipantToChat(ChatModel chat, UserModel user) {
        ChatParticipantModel participant = new ChatParticipantModel();
        participant.setChat(chat);
        participant.setUser(user);
        participantRepository.save(participant);
    }

    private boolean isUserParticipant(ChatModel chat, UserModel user) {
        return participantRepository.findByChatAndUser(chat, user)
            .map(ChatParticipantModel::isActive)
            .orElse(false);
    }

    private ChatDTO convertToChatDTO(ChatModel chat, UserModel currentUser) {
        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());
        dto.setName(chat.getName());
        dto.setGroup(chat.isGroup());
        dto.setCreatedAt(chat.getCreatedAt());
        dto.setUpdatedAt(chat.getUpdatedAt());

        // Participantes
        List<ChatParticipantModel> participants = participantRepository.findByChatAndIsActiveTrue(chat);
        dto.setParticipants(participants.stream()
            .map(this::convertToParticipantDTO)
            .collect(Collectors.toList()));

        // Última mensagem
        List<MessageModel> recentMessages = messageRepository.findRecentMessagesByChat(chat, PageRequest.of(0, 1));
        if (!recentMessages.isEmpty()) {
            dto.setLastMessage(convertToMessageDTO(recentMessages.get(0)));
        }

        // Contagem de não lidas
        dto.setUnreadCount(getUnreadCount(chat.getId(), currentUser));

        // Para chats diretos, definir o nome como o nome do outro usuário
        if (!chat.isGroup() && participants.size() == 2) {
            UserModel otherUser = participants.stream()
                .map(ChatParticipantModel::getUser)
                .filter(user -> !user.getId().equals(currentUser.getId()))
                .findFirst()
                .orElse(null);
            
            if (otherUser != null) {
                dto.setName(otherUser.getLogin());
            }
        }

        return dto;
    }

    private ChatParticipantDTO convertToParticipantDTO(ChatParticipantModel participant) {
        ChatParticipantDTO dto = new ChatParticipantDTO();
        dto.setId(participant.getId());
        dto.setUserId(participant.getUser().getId());
        dto.setUsername(participant.getUser().getLogin());
        dto.setProfilePicture(participant.getUser().getProfilePicture());
        dto.setJoinedAt(participant.getJoinedAt());
        dto.setLastReadAt(participant.getLastReadAt());
        dto.setActive(participant.isActive());
        return dto;
    }

    private MessageDTO convertToMessageDTO(MessageModel message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setChatId(message.getChat().getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderUsername(message.getSender().getLogin());
        dto.setSenderProfilePicture(message.getSender().getProfilePicture());
        dto.setContent(message.getContent());
        dto.setType(message.getType());
        dto.setSentAt(message.getSentAt());
        dto.setEditedAt(message.getEditedAt());
        dto.setDeleted(message.isDeleted());
        return dto;
    }
}