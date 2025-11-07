package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.book.storage.StorageService;
import com.projeto_integrado_biblioteca.domains.genre.Genre;
import com.projeto_integrado_biblioteca.domains.genre.GenreService;
import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.exceptions.InvalidRequestException;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final BookMapper bookMapper;
    private final StorageService storageService;

    @Transactional
    public BookResponse createBook(BookCreateRequest request, MultipartFile pdf, MultipartFile image) {
        if (bookRepository.findByTitle(request.title()).isPresent() || bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new ConflictException(
                    "O livro com titulo: " + request.title() + "; ou com a ISBN: " + request.isbn() + "; já está registrado"
            );
        }

        BookType bt = BookType.getBookOfId(request.bookTypeId());
        if (bt == null) {
            throw new InvalidRequestException("ID de tipo de livro inválido: " + request.bookTypeId());
        }

        Set<Genre> genres = request
                .genres()
                .stream()
                .map(genreService::findByName)
                .collect(Collectors.toSet());

        String pdfKey = storageService.uploadPdf(pdf);
        String imageKey = storageService.uploadImage(image);

        Book book = bookMapper.createRequestToBook(request);
        book.setPdfKey(pdfKey);
        book.setCoverPath(imageKey);
        book.setFileSize(pdf.getSize());
        book.setCopiesAvailable((bt.equals(BookType.LOAN)) ? request.copiesAvailable() : 0);
        book.setCopiesOnLoan(0);
        book.setGenres(genres);
        book.setType(bt);

        Book savedBook = bookRepository.save(book);

        return bookMapper.bookToBookResponse(savedBook);
    }

    @Transactional
    public BookResponse updateBook(Long id, BookUpdateRequest request) {
        Book bookToUpdate = bookRepository.findByIdWithGenres(id).orElseThrow(() -> new IllegalArgumentException("Livro não encontrado"));

        bookMapper.updateBookFromRequest(request, bookToUpdate);

        if (request.genres() != null && !request.genres().isEmpty()) {
            Set<Genre> newGenres = request.genres()
                    .stream()
                    .map(genreService::findByName)
                    .collect(Collectors.toSet());
            bookToUpdate.setGenres(newGenres);
        }

        return bookMapper.bookToBookResponse(bookToUpdate);
    }

    @Transactional(readOnly = true)
    public List<BookResponse> listBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::bookToBookResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookPreviewResponse> listBookPreviews() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::bookToBookPreview)
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDetailsResponse getDetails(Long id) {
        Book book = bookRepository.findByIdWithGenres(id).orElseThrow(
                () -> new ResourceNotFoundException("O livro não encontrado com id: " + id)
        );

        return bookMapper.bookToBookDetails(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado com o id: " + id)
        );
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findByIdWithGenres(id).orElseThrow(
                () -> new ResourceNotFoundException("O livro não encontrado com id: " + id)
        );

        for (Genre g : book.getGenres()) {
            g.getBooks().remove(book);
        }
        book.getGenres().clear();

        bookRepository.delete(book);
    }
}
