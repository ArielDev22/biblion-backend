package com.projeto_integrado_biblioteca.domains.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record BookUpdateRequest(
        @NotBlank
        String title,
        @NotBlank
        String author,
        @NotBlank
        String description,
        @NotNull
        @PositiveOrZero
        Integer year,
        @NotEmpty
        List<String> genres
) {
}
