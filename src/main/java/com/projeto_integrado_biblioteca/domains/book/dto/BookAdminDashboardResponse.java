package com.projeto_integrado_biblioteca.domains.book.dto;

public record BookAdminDashboardResponse(
        Long id,
        String title,
        String isbn,
        String type,
        String imageKey,
        String pdfKey,
        Long pdfSize
) {
}
