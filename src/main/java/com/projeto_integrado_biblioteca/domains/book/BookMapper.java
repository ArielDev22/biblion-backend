package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.genre.GenreMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface BookMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageKey", ignore = true)
    @Mapping(target = "copiesOnLoan", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "type", ignore = true)
    Book createRequestToBook(BookCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateBookFromRequest(BookUpdateRequest request, @MappingTarget Book book);

    @Mapping(target = "pdfKey", source = "pdfFile.pdfKey")
    @Mapping(target = "pdfSize", source = "pdfFile.size")
    BookAdminDashboardResponse toAdminDashboardResponse(Book book);

    BookUserHomeResponse toBookUserHomeResponse(Book book);

    BookDetailsResponse toBookDetails(Book book);
}
