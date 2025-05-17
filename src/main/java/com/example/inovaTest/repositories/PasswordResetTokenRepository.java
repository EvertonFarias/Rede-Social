package com.example.inovaTest.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.inovaTest.models.PasswordResetToken;
import com.example.inovaTest.models.UserModel;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    
    // Adicionar método para encontrar token por usuário
    Optional<PasswordResetToken> findByUser(UserModel user);
    
    // Retornar o número de registros excluídos
    @Modifying
    int deleteByExpiryDateBefore(LocalDateTime now);
}