package com.example.inovaTest.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.inovaTest.models.UserModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getLogin())
                    .withClaim("id", user.getId().toString())
                    .withClaim("role", user.getRole().name()) 
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }
    

public String validateToken(String token){
    try {
        System.out.println("=== Token Validation Debug ===");
        System.out.println("Token recebido: " + (token != null ? "Presente" : "Nulo"));
        
        if (token == null || token.isEmpty()) {
            System.out.println("Token vazio ou nulo");
            return "";
        }
        
        Algorithm algorithm = Algorithm.HMAC256(secret);
        String subject = JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getSubject();
                
        System.out.println("Token válido para usuário: " + subject);
        return subject;
        
    } catch (JWTVerificationException exception){
        System.out.println("Erro na validação do token: " + exception.getMessage());
        return "";
    }
}

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
