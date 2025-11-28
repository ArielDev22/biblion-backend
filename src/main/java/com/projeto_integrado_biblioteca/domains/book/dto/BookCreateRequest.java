package com.projeto_integrado_biblioteca.domains.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BookCreateRequest(
        @NotBlank(message = "Insira o titulo")
        String title,
        @NotBlank(message = "Insira a isbn")
        @Size(min = 13, max = 13, message = "A isbn deve ter somente 13 caracteres")
        String isbn,
        @NotBlank(message = "Insira o autor")
        String author,
        String description,
        Integer year,
        @NotNull(message = "Insira o id de tipo de livro")
        Long bookTypeId,
        @NotEmpty(message = "Insira ao menos um gênero")
        List<Long> genres,
        Integer copiesAvailable
) {
}
