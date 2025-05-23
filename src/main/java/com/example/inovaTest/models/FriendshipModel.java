package com.example.inovaTest.models;

import com.example.inovaTest.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friendships")
public class FriendshipModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private UserModel sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserModel receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    private LocalDateTime createdAt;

    public FriendshipModel(UserModel sender, UserModel receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = FriendshipStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isAccepted() {
        return status == FriendshipStatus.ACCEPTED;
    }
}
