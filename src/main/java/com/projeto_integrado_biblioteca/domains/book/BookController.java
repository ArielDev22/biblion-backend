package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
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

    @GetMapping(value = "/read/free/{id}")
    public ResponseEntity<Resource> readFreeBook(@PathVariable Long id) {
        BookReadPDFResponse response = bookService.getBookPdf(id);

        ContentDisposition disposition = ContentDisposition
                .inline()
                .filename(response.getFilename(), StandardCharsets.UTF_8)
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(disposition);
        headers.setContentType(BookReadPDFResponse.CONTENT_TYPE);
        headers.setContentLength(response.getContentLength());

        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(response.getInputStream()));
    }

    @GetMapping(value = "/details-read", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookReadDetails> getBookReadDetails(@PathParam(value = "id") Long id) {
        return ResponseEntity.ok(bookService.getBookReadDetails(id));
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookAdminDashboardResponse> updateBook(
            @PathVariable Long id,
            @RequestPart("data") @Valid BookUpdateRequest request,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(bookService.updateBook(id, request, pdf, image));
    }

    @PatchMapping(value = "/{id}/update-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateBookPDF(
            @PathVariable Long id,
            @RequestPart(value = "pdf") MultipartFile pdf
    ) {
        return ResponseEntity.ok(bookService.updateBookPDF(id, pdf));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
