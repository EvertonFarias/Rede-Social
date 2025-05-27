package com.example.inovaTest.models;

import com.example.inovaTest.enums.NotificationType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class NotificationModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private UserModel recipient;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false, length = 500)
    private String message;
    
    @Column(name = "reference_id")
    private UUID referenceId; // ID do post, comentário, etc.
    
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public NotificationModel() {}
    
    public NotificationModel(UserModel recipient, UserModel sender, NotificationType type, 
                           String title, String message) {
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.title = title;
        this.message = message;
        this.isRead = false;
    }
    
    public NotificationModel(UserModel recipient, UserModel sender, NotificationType type, 
                           String title, String message, UUID referenceId) {
        this.recipient = recipient;
        this.sender = sender;
        this.type = type;
        this.title = title;
        this.message = message;
        this.referenceId = referenceId;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public UUID getId() {
        return id;
    }
    
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UserModel getRecipient() {
        return recipient;
    }
    
    public void setRecipient(UserModel recipient) {
        this.recipient = recipient;
    }
    
    public UserModel getSender() {
        return sender;
    }
    
    public void setSender(UserModel sender) {
        this.sender = sender;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public UUID getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "NotificationModel{" +
                "id=" + id +
                ", type=" + type +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}