package com.example.inovaTest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inovaTest.dtos.user.ProfilePictureDTO;
import com.example.inovaTest.dtos.user.UserResponseDTO;
import com.example.inovaTest.dtos.user.UserSearchResponseDTO;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id) {
        return userRepository.findById(id)
            .map(user -> new UserResponseDTO(
                user.getId(),
                user.getLogin(),
                user.getEmail(),
                user.getRole(),
                user.isVerifiedEmail(),
                user.getGender(),
                user.getDateOfBirth(),
                user.isEnabled(),
                user.getProfilePicture()
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/{username}")
    public ResponseEntity<List<UserSearchResponseDTO>> searchUsersByUsername(@PathVariable String username) {
        List<UserModel> users = userRepository.findByLoginContainingIgnoreCase(username);

        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<UserSearchResponseDTO> response = users.stream()
            .map(user -> new UserSearchResponseDTO(
                user.getId(),
                user.getLogin(),
                user.getProfilePicture()
            ))
            .toList();

        return ResponseEntity.ok(response);
    }


    
 

    @PutMapping("/{id}/profile-pic")
    public ResponseEntity putProfilePicturePath(@PathVariable UUID id, @RequestBody ProfilePictureDTO entity) {
        Optional<UserModel> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            return ResponseEntity.notFound().build();
        }
        UserModel user = userOptional.get();
        user.setProfilePicture(entity.getProfilePicture());
        userRepository.save(user);
        UserResponseDTO responseDTO = new UserResponseDTO(
            user.getId(),
            user.getProfilePicture()
        );
        return ResponseEntity.ok(responseDTO);
    }
    

    @GetMapping("/search/{query}/exclude/{userId}")
public ResponseEntity<List<UserSearchResponseDTO>> searchUsersByUsernameExcludingCurrent(
        @PathVariable String query,
        @PathVariable UUID userId) {

    List<UserModel> users = userRepository.findByLoginContainingIgnoreCase(query);

    List<UserSearchResponseDTO> response = users.stream()
        .filter(user -> !user.getId().equals(userId)) 
        .map(user -> new UserSearchResponseDTO(
            user.getId(),
            user.getLogin(),
            user.getProfilePicture()
        ))
        .toList();

    if (response.isEmpty()) {
        return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(response);
}

    
}
