package com.projeto_integrado_biblioteca.domains.session.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LastReadBookResponse {
    private Long id;
    private Long progress;
    private Integer currentPage;
    private Integer totalPages;
    private LocalDate lastReadDate;
    private Long bookId;
    private String title;
    private String author;
    private String imageURL;
}

