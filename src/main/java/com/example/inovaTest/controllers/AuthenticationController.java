package com.example.inovaTest.controllers;

import jakarta.validation.Valid;

import java.util.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inovaTest.dtos.auth.AuthenticationDTO;
import com.example.inovaTest.dtos.auth.LoginResponseDTO;
import com.example.inovaTest.dtos.auth.RegisterDTO;
import com.example.inovaTest.dtos.user.UserResponseDTO;
import com.example.inovaTest.enums.GenderRole;
import com.example.inovaTest.enums.UserRole;
import com.example.inovaTest.exceptions.ConflictException;
import com.example.inovaTest.infra.security.TokenService;
import com.example.inovaTest.models.EmailVerificationToken;
import com.example.inovaTest.models.UserModel;
import com.example.inovaTest.repositories.EmailVerificationTokenRepository;
import com.example.inovaTest.repositories.UserRepository;
import com.example.inovaTest.services.AuthService;
import com.example.inovaTest.services.EmailService;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthService authService;
    @Autowired
    private EmailVerificationTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO data){
        try {
        UserModel newUser = authService.registerUser(data);
        UserResponseDTO responseDTO = new UserResponseDTO(
            newUser.getId(),
            newUser.getLogin(),
            newUser.getEmail(),
            newUser.getRole(),
            newUser.isVerifiedEmail(),
            newUser.getGender(),
            newUser.getDateOfBirth(),
            newUser.isEnabled()
        );

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, newUser);
        tokenRepository.save(verificationToken);


        System.out.println("Preparando para enviar e-mail de verificação para: " + newUser.getEmail());
        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;

        // HTML formatado para o e-mail
        String htmlContent = """
            <html>
            <body style="font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #fff; border-radius: 10px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #333;">Verifique seu e-mail</h2>
                    <p>Olá, <strong>%s</strong>!</p>
                    <p>Obrigado por se registrar na <strong>CalangoSocial</strong>. Clique no botão abaixo para verificar seu e-mail:</p>
                    <a href="%s" style="display: inline-block; background-color: #4CAF50; color: white; padding: 12px 20px; border-radius: 5px; text-decoration: none;">Verificar E-mail</a>
                    <p style="margin-top: 20px; font-size: 12px; color: #888;">Se você não se registrou, apenas ignore este e-mail.</p>
                </div>
            </body>
            </html>
        """.formatted(newUser.getLogin(), verificationUrl);

       
        emailService.sendEmail(newUser.getEmail(), "Verificação de E-mail", htmlContent);

        
        return ResponseEntity.ok(responseDTO); 
        } catch (ConflictException e) {
            String errorMessage = e.getMessage(); 
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) { 
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
        }
    }

    @GetMapping("/verify") // rota para verificar o token
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        Optional<EmailVerificationToken> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return ResponseEntity.badRequest().body("Token inválido.");
        }

        EmailVerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expirado.");
        }

        UserModel user = verificationToken.getUser();
        user.setVerifiedEmail(true);
        userRepository.save(user); 

        tokenRepository.delete(verificationToken);

        return ResponseEntity.ok("E-mail verificado com sucesso.");
    }
}
