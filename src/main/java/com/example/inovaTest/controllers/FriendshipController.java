package com.example.inovaTest.controllers;

import com.example.inovaTest.enums.FriendshipStatus;
import com.example.inovaTest.enums.NotificationType;
import com.example.inovaTest.models.FriendshipModel;
import com.example.inovaTest.models.NotificationModel;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.FriendshipRepository;
import com.example.inovaTest.repositories.NotificationRepository;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.services.websocket.NotificationWebSocketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("users/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationWebSocketService notificationWebSocketService;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/request")
public ResponseEntity<?> sendFriendRequest(@RequestParam UUID senderId, @RequestParam UUID receiverId) {
    try {
        System.out.println("=== Enviando solicitação de amizade ===");
        System.out.println("Sender ID: " + senderId);
        System.out.println("Receiver ID: " + receiverId);
        
        // Verificar se os usuários existem
        Optional<UserModel> sender = userRepository.findById(senderId);
        Optional<UserModel> receiver = userRepository.findById(receiverId);

        if (sender.isEmpty() || receiver.isEmpty()) {
            System.out.println("Usuário não encontrado");
            return ResponseEntity.badRequest().body("Usuário não encontrado.");
        }

        System.out.println("Usuários encontrados - Sender: " + sender.get().getUsername() + 
                          ", Receiver: " + receiver.get().getUsername());

        // Verificar se o usuário está tentando adicionar a si mesmo
        if (senderId.equals(receiverId)) {
            return ResponseEntity.badRequest().body("Não é possível enviar solicitação para si mesmo.");
        }

        // Verificar se já existe uma amizade entre os usuários (em qualquer direção)
        Optional<FriendshipModel> existingFriendship = friendshipRepository
            .findBySenderAndReceiver(sender.get(), receiver.get())
            .or(() -> friendshipRepository.findBySenderAndReceiver(receiver.get(), sender.get()));

        if (existingFriendship.isPresent()) {
            System.out.println("Amizade existente encontrada");
            FriendshipModel friendship = existingFriendship.get();
            
            switch (friendship.getStatus()) {
                case PENDING:
                    return ResponseEntity.badRequest().body("Já existe uma solicitação de amizade pendente entre esses usuários.");
                case ACCEPTED:
                    return ResponseEntity.badRequest().body("Os usuários já são amigos.");
                case DECLINED:
                    System.out.println("Atualizando amizade previamente recusada");
                    friendship.setSender(sender.get());
                    friendship.setReceiver(receiver.get());
                    friendship.setStatus(FriendshipStatus.PENDING);
                    friendship.setCreatedAt(LocalDateTime.now());
                    friendshipRepository.save(friendship);
                    
                    System.out.println("Criando notificação...");
                    try {
                        NotificationModel notification = new NotificationModel(
                                receiver.get(),
                                sender.get(),
                                NotificationType.FRIENDSHIP_REQUEST,
                                "Solicitação de amizade",
                                sender.get().getUsername() + " enviou uma solicitação de amizade.",
                                null
                        );
                        System.out.println("Notificação criada, enviando via WebSocket...");
                        this.notificationRepository.save(notification);
                        notificationWebSocketService.sendNotification(notification);
                        System.out.println("Notificação enviada com sucesso");
                    } catch (Exception e) {
                        System.err.println("Erro ao criar/enviar notificação: " + e.getMessage());
                        e.printStackTrace();
                        // Continua mesmo com erro na notificação
                    }
                    
                    return ResponseEntity.ok(friendship);
            }
        }

        System.out.println("Criando nova amizade");
        // Criar nova amizade se não existir nenhuma
        FriendshipModel friendship = new FriendshipModel(sender.get(), receiver.get());
        friendshipRepository.save(friendship);
        System.out.println("Amizade salva no banco");

        System.out.println("Criando notificação...");
        try {
            NotificationModel notification = new NotificationModel(
                    receiver.get(),
                    sender.get(),
                    NotificationType.FRIENDSHIP_REQUEST,
                    "Solicitação de amizade",
                    sender.get().getUsername() + " enviou uma solicitação de amizade.",
                    null
            );
            System.out.println("Notificação criada, enviando via WebSocket...");
            notificationWebSocketService.sendNotification(notification);
            this.notificationRepository.save(notification);
            
            System.out.println("Notificação enviada com sucesso");
        } catch (Exception e) {
            System.err.println("Erro ao criar/enviar notificação: " + e.getMessage());
            e.printStackTrace();
            // Continua mesmo com erro na notificação
        }

        System.out.println("Solicitação processada com sucesso");
        return ResponseEntity.ok(friendship);
        
    } catch (Exception e) {
        System.err.println("Erro geral no processamento da solicitação: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(500).body("Erro interno do servidor: " + e.getMessage());
    }
}

    // Aceitar convite de amizade
    @PutMapping("/{id}/accept")
    public ResponseEntity<?> acceptFriendRequest(@PathVariable UUID id) {
    Optional<FriendshipModel> friendshipOpt = friendshipRepository.findById(id);
    if (friendshipOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    FriendshipModel friendship = friendshipOpt.get();

    // Verificar se a amizade está pendente
    if (friendship.getStatus() != FriendshipStatus.PENDING) {
        return ResponseEntity.badRequest().body("Esta solicitação não está pendente.");
    }

    friendship.setStatus(FriendshipStatus.ACCEPTED);
    friendshipRepository.save(friendship);

    // Criar notificação para o remetente (quem enviou a solicitação originalmente)
    try {
        NotificationModel notification = new NotificationModel(
            friendship.getSender(),   
            friendship.getReceiver(), 
            NotificationType.FRIENDSHIP_ACCEPTED,
            "Solicitação de amizade aceita",
            friendship.getReceiver().getUsername() + " aceitou sua solicitação de amizade.",
            null
        );


        notificationRepository.save(notification);
        notificationWebSocketService.sendNotification(notification);
        System.out.println("Notificação de aceitação enviada com sucesso.");
    } catch (Exception e) {
        System.err.println("Erro ao criar/enviar notificação de aceitação: " + e.getMessage());
        e.printStackTrace();
        // Continua mesmo com erro na notificação
    }

    return ResponseEntity.ok(friendship);
}


    @PutMapping("/{id}/decline")
    public ResponseEntity<?> declineFriendRequest(@PathVariable UUID id) {
        Optional<FriendshipModel> friendshipOpt = friendshipRepository.findById(id);
        if (friendshipOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        FriendshipModel friendship = friendshipOpt.get();
        
        // Verificar se a amizade está pendente
        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            return ResponseEntity.badRequest().body("Esta solicitação não está pendente.");
        }
        
        friendship.setStatus(FriendshipStatus.DECLINED);
        friendshipRepository.save(friendship);
        return ResponseEntity.ok(friendship);
    }

    // Listar amizades de um usuário (todas as amizades onde o usuário participa)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FriendshipModel>> getUserFriendships(@PathVariable UUID userId) {
        Optional<UserModel> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        // Buscar todas as amizades onde o usuário é sender ou receiver
        List<FriendshipModel> friendships = friendshipRepository
            .findBySenderOrReceiver(user.get(), user.get());
        return ResponseEntity.ok(friendships);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriendship(
            @PathVariable UUID id,
            @RequestHeader("userId") UUID userId
    ) {
        Optional<FriendshipModel> friendshipOpt = friendshipRepository.findById(id);
        
        if (friendshipOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FriendshipModel friendship = friendshipOpt.get();

        // Verificar se o usuário tem permissão para deletar (deve ser sender ou receiver)
        boolean isAuthorized = friendship.getSender().getId().equals(userId) ||
                            friendship.getReceiver().getId().equals(userId);

        if (!isAuthorized) {
            return ResponseEntity.status(403).body("Você não tem permissão para excluir esta amizade.");
        }

        friendshipRepository.delete(friendship);
        return ResponseEntity.ok("Solicitação de amizade cancelada ou amizade removida com sucesso.");
    }
}