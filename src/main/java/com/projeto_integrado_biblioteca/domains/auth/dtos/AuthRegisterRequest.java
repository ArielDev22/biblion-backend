package com.projeto_integrado_biblioteca.domains.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(
        @NotBlank(message = "Insira o nome")
        String firstName,
        @NotBlank(message = "Insira o sobrenome")
        String lastName,
        @NotBlank(message = "Insira o email")
        @Email(
                message = "Insira um email válido",
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        String email,
        @NotBlank(message = "Insira a senha")
        @Size(min = 8, message = "A senha deve ter no minimo 8 carateres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).+$",
                message = "A senha deve conter no mínimo uma letra minúscula, uma letra maiúscula e um número."
        )
        String password,
        @NotBlank(message = "Role de usuário não enviada no corpo da requisição.")
        String role
) {
}
