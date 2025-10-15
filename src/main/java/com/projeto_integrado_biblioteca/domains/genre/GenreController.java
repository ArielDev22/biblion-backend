package com.projeto_integrado_biblioteca.domains.genre;

import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreCreateRequest;
import com.projeto_integrado_biblioteca.domains.genre.dtos.GenreResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/genres")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class GenreController {
    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<List<GenreResponse>> create(@RequestBody @Valid GenreCreateRequest request) {
        genreService.registerNewGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.listGenres());
    }
}
