package com.example.inovaTest.dtos.user;

import java.time.LocalDate;
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
    private LocalDate  dateOfBirth;
    private boolean enabled;
    private String profilePicture;

  
    public UserResponseDTO(UUID id, String login, String email, UserRole role, boolean verifiedEmail, GenderRole gender, LocalDate  dateOfBirth, boolean enabled, String profilePicture) {
        this.id = id;
        this.login = login;
        this.email = email;
        this.role = role;
        this.verifiedEmail = verifiedEmail;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.enabled = enabled;
        this.profilePicture =  profilePicture;
    }

        public UserResponseDTO(UUID id, String profilePicture) {
        this.id = id;
        this.profilePicture = profilePicture;

    }

    

}
