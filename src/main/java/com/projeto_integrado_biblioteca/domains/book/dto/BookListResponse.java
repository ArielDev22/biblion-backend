package com.projeto_integrado_biblioteca.domains.book.dto;

import java.util.List;

public record BookListResponse(
        List<BookResponse> bookResponses
) {
}
