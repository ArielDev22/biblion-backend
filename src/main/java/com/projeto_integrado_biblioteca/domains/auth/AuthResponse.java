package com.projeto_integrado_biblioteca.domains.auth;

public record AuthResponse(
        String token,
        String message
) {
}
