package com.example.inovaTest.seeders;

import java.time.LocalDate;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Component
@Order(1000) // Executa após outros componentes
public class DataSeeder implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (alreadySetup) return;

        try {
            // Verifica se já existem users no banco
            if (userRepository.count() > 0) {
                System.out.println("Database already has users, skipping seeder...");
                alreadySetup = true;
                return;
            }

            createAdminUser();
            createRegularUser();
            
            alreadySetup = true;
            System.out.println("Database seeding completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during database seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createAdminUser() {
        UserModel admin = new UserModel();
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("admin12345"));
        admin.setEmail("admin@calangosocial.com");
        admin.setRole(UserRole.ADMIN);
        admin.setEnabled(true);
        admin.setDateOfBirth(LocalDate.of(1985, 3, 15));
        admin.setGender(GenderRole.OTHER);
        admin.setVerifiedEmail(true);
        
        userRepository.save(admin);
        System.out.println("✓ Admin user created: " + admin.getLogin());
    }

    private void createRegularUser() {
        UserModel user = new UserModel();
        user.setLogin("user");
        user.setPassword(passwordEncoder.encode("user12345"));
        user.setEmail("user@calangosocial.com");
        user.setRole(UserRole.USER);
        user.setEnabled(true);
        user.setDateOfBirth(LocalDate.of(1995, 7, 20));
        user.setGender(GenderRole.OTHER);
        user.setVerifiedEmail(true);
        
        userRepository.save(user);
        System.out.println("✓ Regular user created: " + user.getLogin());
    }
}