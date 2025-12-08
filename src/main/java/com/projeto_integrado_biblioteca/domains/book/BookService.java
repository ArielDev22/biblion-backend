package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.storage.StorageService;
import com.projeto_integrado_biblioteca.domains.genre.Genre;
import com.projeto_integrado_biblioteca.domains.genre.GenreService;
import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.exceptions.InvalidRequestException;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
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
    public BookAdminDashboardResponse createBook(BookCreateRequest request, MultipartFile pdf, MultipartFile image) {
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
                .map(genreService::findGenreById)
                .collect(Collectors.toSet());

        String pdfKey = storageService.uploadPdf(pdf);
        String imageKey = storageService.uploadImage(image);

        Book book = bookMapper.createRequestToBook(request);
        book.setImageKey(imageKey);
        book.setGenres(genres);
        book.setType(bt);
        book.setPdfFile(new BookPdfFile(null, generateFilename(request.title()), pdfKey, pdf.getSize(), null));

        Book savedBook = bookRepository.save(book);

        return bookMapper.toAdminDashboardResponse(savedBook);
    }

    @Transactional(readOnly = true)
    public List<BookAdminDashboardResponse> getBooksForAdminDashboard() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toAdminDashboardResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookUserHomeResponse> getBooksForUserHome() {
        return bookRepository
                .findAll()
                .stream()
                .map(b -> new BookUserHomeResponse(b.getId(), b.getTitle(), b.getAuthor(), getImageURL(b.getImageKey())))
                .toList();
    }

    @Transactional(readOnly = true)
    public BookDetailsResponse getBookDetails(Long id) {
        Book book = bookRepository.findByIdWithGenres(id).orElseThrow(
                () -> new ResourceNotFoundException("O livro não encontrado com id: " + id)
        );

        var bookDetails = bookMapper.toBookDetails(book);
        bookDetails.setImageURL(getImageURL(book.getImageKey()));

        return bookDetails;
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado com o id: " + id)
        );
    }

    @Transactional(readOnly = true)
    public List<BookUserHomeResponse> search(String q) {
        BookQueryParams params = new BookQueryParams(q);

        return bookRepository
                .findAll(BookSpec.findUsingParams(params))
                .stream()
                .map(b -> new BookUserHomeResponse(b.getId(), b.getTitle(), b.getAuthor(), getImageURL(b.getImageKey())))
                .toList();
    }

    @Transactional
    public BookAdminDashboardResponse updateBook(
            Long id,
            BookUpdateRequest request,
            MultipartFile pdf,
            MultipartFile image
    ) {
        Book bookToUpdate = getBookById(id);
        bookMapper.updateBookFromRequest(request, bookToUpdate);

        if (!pdf.isEmpty()) {
            changePdf(bookToUpdate, pdf);
        }

        if (!image.isEmpty()) {
            changeImage(bookToUpdate, image);
        }

        if (request.genres() != null) {
            bookToUpdate.setGenres(updateGenres(request.genres()));
        }

        bookRepository.save(bookToUpdate);

        return bookMapper.toAdminDashboardResponse(bookToUpdate);
    }

    public String updateBookPDF(Long id, MultipartFile pdf) {
        var book = getBookById(id);

        String response = "";

        if (book.getPdfFile() != null) {
            changePdf(book, pdf);
            response = "PDF atualizado";
        } else {
            String key = storageService.uploadPdf(pdf);

            book.setPdfFile(new BookPdfFile(null, generateFilename(book.getTitle()), key, pdf.getSize(), null));
            response = "PDF carregado";
        }

        bookRepository.save(book);

        return response;
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        storageService.deletePdf(book.getPdfFile().getPdfKey());
        storageService.deleteImage(book.getImageKey());

        bookRepository.delete(book);
    }

    private void changeImage(Book book, MultipartFile image) {
        storageService.deleteImage(book.getImageKey());
        book.setImageKey(storageService.uploadImage(image));
    }

    private void changePdf(Book book, MultipartFile pdf) {
        storageService.deletePdf(book.getPdfFile().getPdfKey());

        book.getPdfFile().setPdfKey((storageService.uploadPdf(pdf)));
        book.getPdfFile().setSize(pdf.getSize());
    }

    private Set<Genre> updateGenres(List<String> genres) {
        if (genres.isEmpty()) {
            return Collections.emptySet();
        }

        return genres
                .stream()
                .map(genreService::findByName)
                .collect(Collectors.toSet());
    }

    private String generateFilename(String title) {
        return title.replace(" ", "_").toLowerCase().concat(".pdf");
    }

    @Transactional(readOnly = true)
    public BookReadPDFResponse getBookPdf(Long id) {
        Book book = getBookById(id);

        var inputStream = storageService.getPdf(book.getPdfFile().getPdfKey());

        return new BookReadPDFResponse(inputStream, book.getPdfFile().getFilename(), book.getPdfFile().getSize());
    }

    public String getImageURL(String key) {
        return storageService.getCoverURL(key);
    }

    public BookReadDetails getBookReadDetails(Long id) {
        return bookMapper.toBookReadDetails(getBookById(id));
    }
}
