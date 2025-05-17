package com.example.inovaTest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.inovaTest.dtos.auth.LoginResponseDTO;
import com.example.inovaTest.dtos.user.UserResponseDTO;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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
                user.isEnabled()
            ))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
}
