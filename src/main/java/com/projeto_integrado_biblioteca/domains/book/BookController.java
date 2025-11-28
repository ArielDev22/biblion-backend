package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookAdminDashboardResponse> create(
            @RequestPart("data") @Valid BookCreateRequest request,
            @RequestPart("pdf") MultipartFile pdf,
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request, pdf, image));
    }

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookUserHomeResponse>> listPreviewBooks() {
        return ResponseEntity.ok(bookService.getBooksForUserHome());
    }

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDetailsResponse> getDetails(@RequestParam Long id) {
        return ResponseEntity.ok(bookService.getBookDetails(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<BookAdminDashboardResponse>> listBooks() {
        return ResponseEntity.ok(bookService.getBooksForAdminDashboard());
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookUserHomeResponse>> search(@PathParam("q") String q) {
        return ResponseEntity.ok(bookService.search(q));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookAdminDashboardResponse> update(
            @PathVariable Long id,
            @RequestPart("data") @Valid BookUpdateRequest request,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, request, pdf, image));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
