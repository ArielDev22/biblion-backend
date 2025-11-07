package com.projeto_integrado_biblioteca.domains.book.dto;

public record BookResponse(
        Long id,
        String title,
        String isbn,
        String author,
        Integer year,
        String description,
        String type,
        String coverPath,
        String pdfKey,
        Long fileSize,
        Integer copiesAvailable,
        Integer copiesOnLoan
) {
}
