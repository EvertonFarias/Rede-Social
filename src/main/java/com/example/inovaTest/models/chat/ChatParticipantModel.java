package com.example.inovaTest.models.chat;

import jakarta.persistence.*;
import lombok.*;

import com.example.inovaTest.models.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_participants", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"chat_id", "user_id"}))
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatParticipantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatModel chat;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    private LocalDateTime lastReadAt;

    @Column(nullable = false)
    private boolean isActive; // false se o usuário saiu do chat

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        isActive = true;
    }
}