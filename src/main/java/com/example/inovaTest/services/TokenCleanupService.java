package com.example.inovaTest.services;

import com.example.inovaTest.models.PasswordResetToken;
import com.example.inovaTest.repositories.PasswordResetTokenRepository;
import com.example.inovaTest.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    /**
     * Remove todos os tokens de redefinição de senha expirados.
     * Este método é transacional para garantir a consistência dos dados.
     */
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = resetTokenRepository.deleteByExpiryDateBefore(now);
        System.out.println(String.format("Limpeza de tokens: %d tokens de redefinição de senha expirados foram removidos em: %s", 
                deletedCount, now));
    }
}