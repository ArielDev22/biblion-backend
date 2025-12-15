package com.projeto_integrado_biblioteca.domains.library;

import com.projeto_integrado_biblioteca.domains.library.dtos.LibraryDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/libraries")
@RequiredArgsConstructor
public class LibraryController {

    private final LibraryService libraryService;


    @PostMapping("/{userId}/{bookId}")
    public ResponseEntity<String> addBook(
            @PathVariable Long userId,
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(libraryService.addBook(userId, bookId));
    }


    @DeleteMapping("/{libraryId}/{libraryBookId}")
    public ResponseEntity<Void> removeBook(
            @PathVariable Long libraryId,
            @PathVariable Long libraryBookId
    ) {
        libraryService.removeBook(libraryId, libraryBookId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LibraryDetails> getLibraryDetails(@PathVariable Long id) {
        return ResponseEntity.ok(libraryService.getLibraryDetails(id));
    }
}
