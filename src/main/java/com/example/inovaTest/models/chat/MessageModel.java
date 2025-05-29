package com.example.inovaTest.models.chat;

import jakarta.persistence.*;
import lombok.*;

import com.example.inovaTest.enums.MessageType;
import com.example.inovaTest.models.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messages")
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatModel chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType type; // TEXT, IMAGE, FILE

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private LocalDateTime editedAt;

    @Column(nullable = false)
    private boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
        isDeleted = false;
        if (type == null) {
            type = MessageType.TEXT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (editedAt == null) {
            editedAt = LocalDateTime.now();
        }
    }
}