package com.projeto_integrado_biblioteca.domains.book.dto;

public record BookFileInfo(
        String filename,
        String key,
        Long size
) {
}
