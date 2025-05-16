package com.example.inovaTest.seeders;

 
 
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.models.UserModel;

import com.example.inovaTest.repositories.UserRepository;


@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private boolean alreadySetup = false;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) return;

        // Verifica se já existem roles no banco
        if (userRepository.count() > 0) {
            alreadySetup = true;
            return;
        }
            UserModel admin = new UserModel();
            admin.setLogin("admin");
            admin.setPassword(passwordEncoder.encode("admin12345"));
            admin.setEmail("admin@admin.com");
            admin.setRole(UserRole.ADMIN); // ← transforma em lista
            admin.setEnabled(true);
            userRepository.save(admin);


            System.out.println("Admin user created: " + admin.getLogin());
        alreadySetup = true;
    }
}