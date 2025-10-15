package com.projeto_integrado_biblioteca.domains.book.dto;

public record BookPreviewResponse(
        Long id,
        String title,
        String author,
        String coverPath,
        String description
) {
}
