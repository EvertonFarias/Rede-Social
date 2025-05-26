package com.example.inovaTest.config;

import com.example.inovaTest.infra.security.TokenService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class AuthHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenService tokenService;

    public AuthHandshakeInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        System.out.println("=== WebSocket Handshake Debug ===");
        System.out.println("URI: " + request.getURI());
        
        // Pegar token da query string
        String query = request.getURI().getQuery();
        System.out.println("Query: " + query);
        
        if (query != null && query.contains("token=")) {
            String token = extractTokenFromQuery(query);
            System.out.println("Token extraído: " + (token != null ? "Token presente" : "Token nulo"));
            
            if (token != null) {
                String username = tokenService.validateToken(token);
                System.out.println("Username validado: " + username);
                
                if (!username.isEmpty()) {
                    attributes.put("username", username);
                    System.out.println("Conexão autorizada para: " + username);
                    return true;
                } else {
                    System.out.println("Token inválido ou expirado");
                }
            }
        } else {
            System.out.println("Token não encontrado na query string");
        }
        
        System.out.println("Conexão rejeitada");
        return false; // Rejeita conexão se token inválido
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                              WebSocketHandler wsHandler, Exception exception) {
        // Implementação vazia
    }

    private String extractTokenFromQuery(String query) {
        if (query == null) return null;
        
        String[] params = query.split("&");
        for (String param : params) {
            if (param.startsWith("token=")) {
                String token = param.substring(6); // Remove "token="
                System.out.println("Token extraído (primeiros 20 chars): " + 
                    (token.length() > 20 ? token.substring(0, 20) + "..." : token));
                return token;
            }
        }
        return null;
    }
}