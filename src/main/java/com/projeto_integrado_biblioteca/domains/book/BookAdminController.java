package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.BookCreateRequest;
import com.projeto_integrado_biblioteca.domains.book.dto.BookResponse;
import com.projeto_integrado_biblioteca.domains.book.dto.BookUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/books")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class BookAdminController {
    private final BookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> create(
            @RequestPart("data") @Valid BookCreateRequest request,
            @RequestPart("pdf") MultipartFile pdf,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request, pdf, image));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> listBooks() {
        return ResponseEntity.ok(bookService.listBooks());
    }

    @PutMapping
    public ResponseEntity<BookResponse> update(
            @RequestParam Long id,
            @RequestBody @Valid BookUpdateRequest request
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
