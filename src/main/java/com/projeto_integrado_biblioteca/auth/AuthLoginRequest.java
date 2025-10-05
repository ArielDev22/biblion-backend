package com.projeto_integrado_biblioteca.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        @NotBlank(message = "Insira o email")
        String email,
        @NotBlank(message = "Insira a senha")
        String password
) {
}
