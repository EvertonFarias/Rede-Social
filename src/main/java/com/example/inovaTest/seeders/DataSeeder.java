package com.example.inovaTest.seeders;

import java.time.LocalDate;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;

@Component
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (alreadySetup) return;

        // Verifica se já existem users no banco
        if (userRepository.count() > 0) {
            alreadySetup = true;
            return;
        }

        UserModel admin = new UserModel();
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("admin12345"));
        admin.setEmail("admin@admin.com");
        admin.setRole(UserRole.ADMIN);
        admin.setEnabled(true);
        admin.setDateOfBirth(LocalDate.now());
        admin.setGender(GenderRole.OTHER);
        admin.setVerifiedEmail(true);
        userRepository.save(admin);

        System.out.println("Admin user created: " + admin.getLogin());
        
        UserModel user = new UserModel();
        user.setLogin("user");
        user.setPassword(passwordEncoder.encode("user12345"));
        user.setEmail("user@user.com");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        user.setDateOfBirth(LocalDate.now());
        user.setGender(GenderRole.OTHER);
        user.setVerifiedEmail(true);
        userRepository.save(user);
        
        System.out.println("User user created: " + user.getLogin());

        alreadySetup = true;
    }
}