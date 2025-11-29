package com.projeto_integrado_biblioteca.domains.download;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DownloadMapper {

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    @Mapping(target = "filename", source = "book.pdfFile.filename")
    @Mapping(target = "size", source = "book.pdfFile.size")
    @Mapping(target = "imageURL", ignore = true)
    DownloadHistoricResponse toDownloadHistoric(Download download);
}
