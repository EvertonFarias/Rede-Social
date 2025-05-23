package com.example.inovaTest.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FirebaseInitializationService {

    @PostConstruct
    public void initialize() throws IOException {
                try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase-service-account.json");
            InputStream serviceAccountStream = serviceAccount.getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setStorageBucket("calangosocial-b9713.firebasestorage.app")
                .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase inicializado com sucesso.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Falha ao inicializar Firebase: arquivo firebase-service-account.json não encontrado ou inválido.", e);
        }
    }
}
