package com.projeto_integrado_biblioteca.domains.session.dto;

public record SessionStartRequest(
        Long userId,
        Long bookId,
        Integer totalPages
) {
}
