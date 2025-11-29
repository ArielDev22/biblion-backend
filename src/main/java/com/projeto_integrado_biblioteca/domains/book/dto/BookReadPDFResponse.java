package com.projeto_integrado_biblioteca.domains.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class BookReadPDFResponse {
    public static final MediaType CONTENT_TYPE = MediaType.APPLICATION_PDF;

    private InputStream inputStream;
    private String filename;
    private Long contentLength;
}
