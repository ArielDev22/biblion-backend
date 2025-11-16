package com.projeto_integrado_biblioteca.domains.auth.dtos;

public record AuthUserData(
        String id,
        String name,
        String email,
        String role
) {
}
