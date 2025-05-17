package com.example.inovaTest.dtos.user;

import java.util.Date;
import java.util.UUID;

import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.UserRole;

import lombok.Data;

@Data
public class UserResponseDTO {
    private UUID id;
    private String login;
    private String email;
    private UserRole role;
    private boolean verifiedEmail;
    private GenderRole gender;
    private Date dateOfBirth;
    private boolean enabled;

  
    public UserResponseDTO(UUID id, String login, String email, UserRole role, boolean verifiedEmail, GenderRole gender, java.util.Date dateOfBirth, boolean enabled) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.role = role;
        this.verifiedEmail = verifiedEmail;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.enabled = enabled;
    }

    

}
