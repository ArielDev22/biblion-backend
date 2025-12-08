package com.projeto_integrado_biblioteca.domains.library;

import com.projeto_integrado_biblioteca.domains.book.Book;
import com.projeto_integrado_biblioteca.domains.book.BookService;
import com.projeto_integrado_biblioteca.domains.library.dtos.LibraryBookDetails;
import com.projeto_integrado_biblioteca.domains.library.dtos.LibraryDetails;
import com.projeto_integrado_biblioteca.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class LibraryService {

    private final LibraryBookRepository bookRepository;
    private final LibraryRepository libraryRepository;
    private final BookService bookService;


    @Transactional
    public void addBook(Long libraryId, Long bookId) {
        Book book = bookService.getBookById(bookId);
        Library library = getById(libraryId);

        library.addBook(bookRepository.save(new LibraryBook(null, book, library)));
        libraryRepository.save(library);
    }


    @Transactional
    public void removeBook(Long libraryId, Long libraryBookId) {
        LibraryBook book = bookRepository.findById(libraryBookId).orElseThrow(
                () -> new ResourceNotFoundException("ERRO ao buscar o livro da biblioteca do usuáriio com o id: " + libraryBookId)
        );
        Library library = getById(libraryId);

        library.removeBook(book);

        bookRepository.deleteById(libraryBookId);
        libraryRepository.save(library);
    }


    @Transactional(readOnly = true)
    public LibraryDetails getLibraryDetails(Long id) {
        Library library = getById(id);

        List<LibraryBookDetails> books = library
                .getBooks()
                .stream()
                .map(LibraryBook::getBook)
                .map(b -> new LibraryBookDetails(b.getId(), b.getTitle(), b.getAuthor(), bookService.getImageURL(b.getImageKey())))
                .toList();

        return new LibraryDetails(library.getNumberOfBooks(), books);
    }


    private Library getById(Long libraryId) {
        return libraryRepository.findById(libraryId).orElseThrow(
                () -> new ResourceNotFoundException("ERRO ao buscar a biblioteca do usuário com o id: " + libraryId)
        );
    }
}
