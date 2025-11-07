package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.BookDetailsResponse;
import com.projeto_integrado_biblioteca.domains.book.dto.BookPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping(value = "/home", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookPreviewResponse>> listPreviewBooks() {
        return ResponseEntity.ok(bookService.listBookPreviews());
    }

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookDetailsResponse> getDetails(@RequestParam Long id) {
        return ResponseEntity.ok(bookService.getDetails(id));
    }
}
