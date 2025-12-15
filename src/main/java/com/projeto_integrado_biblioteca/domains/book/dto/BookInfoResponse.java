package com.projeto_integrado_biblioteca.domains.book.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookInfoResponse{
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer year;
    private String description;
    private String type;
    private List<String> genres;
    private BookFileInfo pdfInfo;
    private BookFileInfo imageInfo;
}
