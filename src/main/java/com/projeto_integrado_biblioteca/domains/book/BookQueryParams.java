package com.projeto_integrado_biblioteca.domains.book;

import lombok.Data;

@Data
public class BookQueryParams {
    private String title;
    private String author;
    private String genre;

    public BookQueryParams(String q) {
        this.title = q;
        this.author = q;
        this.genre = q;
    }
}
