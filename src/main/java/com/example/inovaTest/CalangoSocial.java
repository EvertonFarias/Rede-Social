package com.example.inovaTest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.example.inovaTest.repositories.PasswordResetTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableAsync
@EnableScheduling // Adicione esta anotação para garantir que o agendador funcione
public class CalangoSocial {

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    public static void main(String[] args) {
        SpringApplication.run(CalangoSocial.class, args);
    }

    @Scheduled(cron = "0 0 */6 * * *") // Executar a cada 6 horas
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        resetTokenRepository.deleteByExpiryDateBefore(now);
        System.out.println("Tokens de redefinição de senha expirados foram removidos em: " + now);
    }

}