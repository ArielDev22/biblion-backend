package com.projeto_integrado_biblioteca.domains.book.dto;

import com.projeto_integrado_biblioteca.domains.book.BookType;
import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreResponse;

import java.util.List;

public record BookDetailsResponse(
        Long id,
        String title,
        String isbn,
        String author,
        String description,
        Integer year,
        String coverPath,
        Double fileSize,
        BookType type,
        Integer copiesAvailable,
        List<GenreResponse> genres
) {
}
