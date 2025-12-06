package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.session.dto.LastReadBookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessionBookMapper {

    @Mapping(target = "progress", source = "book.progress")
    @Mapping(target = "currentPage", source = "book.currentPage")
    @Mapping(target = "totalPages", source = "book.totalPages")
    @Mapping(target = "lastReadDate", source = "book.lastReadDate")
    @Mapping(target = "bookId", source = "book.book.id")
    @Mapping(target = "title", source = "book.book.title")
    @Mapping(target = "author", source = "book.book.author")
    LastReadBookResponse toLastReadBook(Session session);
}
