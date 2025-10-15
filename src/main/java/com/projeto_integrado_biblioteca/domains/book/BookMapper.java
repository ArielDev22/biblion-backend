package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.genre.GenreMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = GenreMapper.class)
public interface BookMapper {
    @Mapping(target = "bookId", ignore = true)
    @Mapping(target = "pdfPath", ignore = true)
    @Mapping(target = "coverPath", ignore = true)
    @Mapping(target = "fileSize", ignore = true)
    @Mapping(target = "copiesOnLoan", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "type", ignore = true)
    Book createRequestToBook(BookCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "genres", ignore = true)
    void updateBookFromRequest(BookUpdateRequest request, @MappingTarget Book book);

    @Mapping(target = "id", source = "bookId")
    BookResponse bookToBookResponse(Book book);

    @Mapping(target = "id", source = "bookId")
    BookPreviewResponse bookToBookPreview(Book book);

    @Mapping(target = "id", source = "bookId")
    BookDetailsResponse bookToBookDetails(Book book);
}
