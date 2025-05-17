package com.example.inovaTest.models;

import jakarta.persistence.*;


import java.time.LocalDateTime;



@Entity
public class PasswordResetToken {
    
    private static final int EXPIRATION = 24; // horas
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String token;
    
    @OneToOne(targetEntity = UserModel.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id", unique = true)
    private UserModel user;
    
    private LocalDateTime expiryDate;
    
    public PasswordResetToken() {
     
    }
    
    public PasswordResetToken(String token, UserModel user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }
    
    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusHours(EXPIRATION);
    }
    

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryDate);
    }
}


