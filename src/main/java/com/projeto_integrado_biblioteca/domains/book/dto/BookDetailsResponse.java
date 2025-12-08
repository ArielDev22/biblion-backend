package com.projeto_integrado_biblioteca.domains.book.dto;

import com.projeto_integrado_biblioteca.domains.book.BookType;
import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreResponse;
import lombok.Data;

import java.util.List;

@Data
public class BookDetailsResponse {
    private Long id;
    private String title;
    private String isbn;
    private String author;
    private String description;
    private Integer year;
    private String imageURL;
    private Long pdfSize;
    private BookType type;
    private List<GenreResponse> genres;
}
