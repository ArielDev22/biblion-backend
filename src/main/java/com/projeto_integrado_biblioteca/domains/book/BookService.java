package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.book.dto.*;
import com.projeto_integrado_biblioteca.domains.book.mappers.BookMapper;
import com.projeto_integrado_biblioteca.domains.book.models.*;
import com.projeto_integrado_biblioteca.domains.storage.StorageService;
import com.projeto_integrado_biblioteca.domains.genre.Genre;
import com.projeto_integrado_biblioteca.domains.genre.GenreService;
import com.projeto_integrado_biblioteca.exceptions.ConflictException;
import com.projeto_integrado_biblioteca.exceptions.InvalidRequestException;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import com.projeto_integrado_biblioteca.utils.PdfUtils;
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

    //region Dependências

    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final StorageService storageService;
    private final BookMapper bookMapper;
    private final PdfUtils pdfUtils;

    //endregion

    //region Métodos

    @Transactional
    public BookAdminDashboardResponse createBook(BookCreateRequest request, MultipartFile filePDF, MultipartFile fileImage) {
        if (this.bookRepository.findByTitle(request.title()).isPresent() || this.bookRepository.findByIsbn(request.isbn()).isPresent()) {
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
                .map(this.genreService::findByName)
                .collect(Collectors.toSet());

        String pdfKey = this.storageService.uploadPdf(filePDF);
        String imageKey = this.storageService.uploadImage(fileImage);

        BookFilePDF pdf = this.buildBookPDF(pdfKey, request.title(), filePDF.getSize());
        pdf.setNumberOfPages(this.pdfUtils.getNumberOfPages(filePDF));

        BookFileCover cover = this.buildBookCover(imageKey, request.title(), fileImage.getSize());


        Book book = this.bookMapper.createRequestToBook(request);
        book.setGenres(genres);
        book.setType(bt);

        book.setPdf(pdf);
        book.setCover(cover);

        pdf.setBook(book);
        cover.setBook(book);

        Book savedBook = this.bookRepository.save(book);

        return this.bookMapper.toAdminDashboardResponse(savedBook);
    }


    @Transactional(readOnly = true)
    public List<BookAdminDashboardResponse> getBooksForAdminDashboard() {
        return this.bookRepository
                .findAll()
                .stream()
                .map(this.bookMapper::toAdminDashboardResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<BookUserHomeResponse> getBooksForUserHome() {
        return this.bookRepository
                .findAll()
                .stream()
                .map(b -> new BookUserHomeResponse(b.getId(), b.getTitle(), b.getAuthor(), getImageURL(b.getCover().getFileKey())))
                .toList();
    }


    @Transactional(readOnly = true)
    public BookDetailsResponse getBookDetails(Long id) {
        Book book = this.bookRepository.findByIdWithGenres(id).orElseThrow(
                () -> new ResourceNotFoundException("O livro não encontrado com id: " + id)
        );

        var bookDetails = bookMapper.toBookDetails(book);
        bookDetails.setImageURL(getImageURL(book.getCover().getFileKey()));

        return bookDetails;
    }


    public Book getBookById(Long id) {
        return this.bookRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Livro não encontrado com o id: " + id)
        );
    }


    @Transactional(readOnly = true)
    public BookInfoResponse getBookInfoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("O parâmetro de id não pode ser nulo");
        }

        Book book = this.getBookById(id);

        var pdf = book.getPdf();
        BookFileInfo pdfInfo = (pdf != null) ?
                new BookFileInfo(pdf.getFilename(), pdf.getFileKey(), pdf.getSize()) : new BookFileInfo("", "", 0L);

        var cover = book.getCover();
        BookFileInfo coverInfo = (cover != null) ?
                new BookFileInfo(cover.getFilename(), cover.getFileKey(), cover.getSize()) : new BookFileInfo("", "", 0L);

        BookInfoResponse response = bookMapper.toBookInfo(book);
        response.setPdfInfo(pdfInfo);
        response.setImageInfo(coverInfo);
        response.setGenres(book.getGenres().stream().map(Genre::getName).toList());

        return response;
    }


    @Transactional(readOnly = true)
    public BookReadPDFResponse getBookPdf(Long id) {
        Book book = this.getBookById(id);

        var pdf = book.getPdf();
        var inputStream = this.storageService.getPdf(pdf.getFileKey());

        return new BookReadPDFResponse(inputStream, pdf.getFilename(), pdf.getSize());
    }


    public String getImageURL(String key) {
        return this.storageService.getCoverURL(key);
    }


    public BookReadDetails getBookReadDetails(Long id) {
        return this.bookMapper.toBookReadDetails(this.getBookById(id));
    }

    @Transactional(readOnly = true)
    public List<BookUserHomeResponse> search(String q) {
        BookQueryParams params = new BookQueryParams(q);

        return this.bookRepository
                .findAll(BookSpec.findUsingParams(params))
                .stream()
                .map(b -> new BookUserHomeResponse(b.getId(), b.getTitle(), b.getAuthor(), this.getImageURL(b.getCover().getFileKey())))
                .toList();
    }


    @Transactional
    public BookAdminDashboardResponse updateBook(
            Long id,
            BookUpdateRequest request,
            MultipartFile pdf,
            MultipartFile image
    ) {
        Book bookToUpdate = this.getBookById(id);
        this.bookMapper.updateBookFromRequest(request, bookToUpdate);

        if (pdf != null && !pdf.isEmpty()) {
            this.changePdf(bookToUpdate, pdf);
        }

        if (image != null && !image.isEmpty()) {
            this.changeImage(bookToUpdate, image);
        }

        if (request.genres() != null) {
            bookToUpdate.setGenres(updateGenres(request.genres()));
        }

        this.bookRepository.save(bookToUpdate);

        return this.bookMapper.toAdminDashboardResponse(bookToUpdate);
    }


    public String updateBookPDF(Long id, MultipartFile pdf) {
        Book book = getBookById(id);

        String response = "";

        if (book.getPdf() != null) {
            this.changePdf(book, pdf);
            response = "PDF atualizado";
        } else {
            String key = this.storageService.uploadPdf(pdf);

            book.setPdf(this.buildBookPDF(key, book.getTitle(), pdf.getSize()));
            response = "PDF carregado";
        }

        this.bookRepository.save(book);

        return response;
    }


    @Transactional
    public void deleteBook(Long id) {
        Book book = getBookById(id);

        if (book.getPdf() != null){
            this.storageService.deletePdf(book.getPdf().getFileKey());
        }

        if (book.getCover() != null){
            this.storageService.deleteImage(book.getCover().getFileKey());
        }

        this.bookRepository.delete(book);
    }

    //endregion

    //region Métodos Privados

    private void changeImage(Book book, MultipartFile image) {
        this.storageService.deleteImage(book.getCover().getFileKey());
        String key = this.storageService.uploadImage(image);

        book.setCover(buildBookCover(key, book.getTitle(), image.getSize()));
    }


    private void changePdf(Book book, MultipartFile pdf) {
        BookFilePDF actualPdf = book.getPdf();

        if (actualPdf != null){
            this.storageService.deletePdf(book.getPdf().getFileKey());
        }

        String key = this.storageService.uploadPdf(pdf);

        BookFilePDF filePDF = buildBookPDF(key, book.getTitle(), pdf.getSize());
        book.setPdf(filePDF);
        filePDF.setBook(book);
    }


    private Set<Genre> updateGenres(List<String> genres) {
        if (genres.isEmpty()) {
            return Collections.emptySet();
        }

        return genres
                .stream()
                .map(this.genreService::findByName)
                .collect(Collectors.toSet());
    }


    private String generatePDFFilename(String title) {
        return title.replace(" ", "_").toLowerCase().concat(".pdf");
    }


    private String generateCoverFilename(String title) {
        return title.replace(" ", "_").toLowerCase().concat(".jpg");
    }

    private BookFileCover buildBookCover(String key, String title, long size) {
        var cover = new BookFileCover();
        cover.setFileKey(key);
        cover.setFilename(this.generateCoverFilename(title));
        cover.setSize(size);

        return cover;
    }


    private BookFilePDF buildBookPDF(String key, String title, long size) {
        var pdf = new BookFilePDF();
        pdf.setFileKey(key);
        pdf.setFilename(generatePDFFilename(title));
        pdf.setSize(size);

        return pdf;
    }

    //endregion
}
