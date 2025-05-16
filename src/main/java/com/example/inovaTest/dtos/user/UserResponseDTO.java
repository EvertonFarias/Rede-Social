package com.example.inovaTest.dtos.user;

import java.util.UUID;

import com.example.inovaTest.enums.UserRole;

import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    private String login;
    private String email;
    private UserRole role;
    private boolean verifiedEmail;

    // Construtor
    public UserResponseDTO(UUID id, String login, String email, UserRole role, boolean verifiedEmail) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.role = role;
        this.verifiedEmail = verifiedEmail;
    }

    

}
