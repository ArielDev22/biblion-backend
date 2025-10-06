package com.projeto_integrado_biblioteca.auth;

public record AuthResponse(
        String token,
        String userRole,
        String userEmail,
        String message
) {
}
