package com.example.inovaTest.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.inovaTest.models.NotificationModel;

public interface NotificationRepository extends JpaRepository<NotificationModel, UUID> {
    
}
