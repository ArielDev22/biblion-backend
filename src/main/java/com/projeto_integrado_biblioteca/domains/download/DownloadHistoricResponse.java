package com.projeto_integrado_biblioteca.domains.download;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.UUID;

public record DownloadHistoricResponse(
        UUID id,
        String fileName,
        String book,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        LocalDate downloadedAt,
        Long size
) {
}
