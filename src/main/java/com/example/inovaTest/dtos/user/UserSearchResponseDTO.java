package com.example.inovaTest.dtos.user;

import java.util.UUID;

import lombok.Data;

@Data
public class UserSearchResponseDTO {
    private UUID id;
    private String username;
    private String profilePicture;

    public UserSearchResponseDTO(UUID id, String login, String profilePicture) {
        this.id = id;
        this.username = login;
        this.profilePicture = profilePicture;
    }
    
}
