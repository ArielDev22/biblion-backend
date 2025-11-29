package com.projeto_integrado_biblioteca.domains.download;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class DownloadHistoricResponse {
    private UUID id;

    private String filename;

    private Long bookId;

    private String bookTitle;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate downloadedAt;

    private Long size;

    private String imageURL;
}
