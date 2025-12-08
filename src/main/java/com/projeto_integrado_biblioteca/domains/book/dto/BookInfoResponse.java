package com.projeto_integrado_biblioteca.domains.book.dto;

import java.util.List;

public record BookInfoResponse(
        Long id,
        String title,
        String author,
        String isbn,
        Integer year,
        String description,
        String type,
        List<String> genres,
        BookFileInfo pdfInfo,
        BookFileInfo imageInfo
) {
}
