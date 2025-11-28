package com.projeto_integrado_biblioteca.domains.auth.dtos;

public record AuthResponse(
        String accessToken,
        AuthUserData userData
) {
}
