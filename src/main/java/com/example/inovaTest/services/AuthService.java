package com.example.inovaTest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.inovaTest.dtos.auth.RegisterDTO;
import com.example.inovaTest.exceptions.ConflictException;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserModel registerUser(RegisterDTO data) throws ConflictException {
        // Verificar se o usuário já existe
        if (userRepository.findByLogin(data.login()) != null) {
            throw new ConflictException("User already exists.");
        }
        if (userRepository.findByEmail(data.email()) != null) {
            throw new ConflictException("Email already exists.");
        }
        String encryptedPassword = passwordEncoder.encode(data.password());
        UserModel newUser = new UserModel(data.login(), encryptedPassword, data.email(), data.gender(), data.dateOfBirth());
        userRepository.save(newUser);

        return newUser; 
    }
}
