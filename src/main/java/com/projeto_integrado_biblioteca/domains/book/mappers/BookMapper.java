package com.projeto_integrado_biblioteca.domains.book.mappers;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.book.models.Book;
import com.projeto_integrado_biblioteca.domains.genre.GenreMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "type", ignore = true)
    Book createRequestToBook(BookCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateBookFromRequest(BookUpdateRequest request, @MappingTarget Book book);

    @Mapping(target = "pdfKey", source = "pdf.fileKey")
    @Mapping(target = "pdfSize", source = "pdf.size")
    @Mapping(target = "imageKey", source = "cover.fileKey")
    BookAdminDashboardResponse toAdminDashboardResponse(Book book);

    BookUserHomeResponse toBookUserHomeResponse(Book book);

    @Mapping(target = "pdfSize", source = "pdf.size")
    BookDetailsResponse toBookDetails(Book book);

    BookReadDetails toBookReadDetails(Book book);

    @Mapping(target = "pdfInfo", ignore = true)
    @Mapping(target = "imageInfo", ignore = true)
    @Mapping(target = "genres", ignore = true)
    BookInfoResponse toBookInfo(Book book);
}
