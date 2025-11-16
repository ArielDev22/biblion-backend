package com.projeto_integrado_biblioteca.domains.genre;

import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreCreateRequest;
import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "books", ignore = true)
    Genre genreCreateRequestToGenre(GenreCreateRequest request);

    GenreResponse genreToGenreResponse(Genre genre);
}
