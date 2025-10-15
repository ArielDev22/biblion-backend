package com.projeto_integrado_biblioteca.domains.genre.dtos;

import jakarta.validation.constraints.NotBlank;

public record GenreCreateRequest(
        @NotBlank(message = "Insira o nome do gênero")
        String name
) {
}
