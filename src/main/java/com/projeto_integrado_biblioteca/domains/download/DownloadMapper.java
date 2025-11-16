package com.projeto_integrado_biblioteca.domains.download;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DownloadMapper {

    @Mapping(target = "book", source = "book.title")
    @Mapping(target = "fileName", source = "book.pdfFile.filename")
    @Mapping(target = "size", source = "book.pdfFile.size")
    DownloadHistoricResponse toDownloadHistoric(Download download);
}
