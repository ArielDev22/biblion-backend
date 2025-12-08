package com.projeto_integrado_biblioteca.domains.library.dtos;

public record LibraryBookDetails(
        Long bookId,
        String title,
        String author,
        String imageURL
) {
}
