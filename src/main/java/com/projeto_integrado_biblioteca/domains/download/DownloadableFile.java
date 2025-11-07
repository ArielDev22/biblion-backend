package com.projeto_integrado_biblioteca.domains.download;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class DownloadableFile {
    private InputStream inputStream;
    private String fileName;
    private String contentType;
    private Long contentLength;
}
