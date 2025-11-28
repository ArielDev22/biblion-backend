package com.projeto_integrado_biblioteca.domains.genre;

import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreCreateRequest;
import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreResponse;
import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Transactional
    public void registerNewGenre(GenreCreateRequest request) {
        if (genreRepository.existsByNameIgnoreCase(request.name())) {
            throw new ConflictException("Já existe um gênero com o nome: " + request.name());
        }
        Genre genre = genreMapper.genreCreateRequestToGenre(request);

        genreRepository.save(genre);
    }

    @Transactional(readOnly = true)
    public List<GenreResponse> listGenres() {
        return genreRepository
                .findAll()
                .stream()
                .map(genreMapper::genreToGenreResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Genre findByName(String name) {
        return genreRepository.findByName(name).orElse(null);
    }

    public Genre findGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Genero não encontrado com o id: " + id));
    }
}
