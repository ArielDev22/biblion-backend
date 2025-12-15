package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.book.models.Book;
import com.projeto_integrado_biblioteca.domains.book.BookService;
import com.projeto_integrado_biblioteca.domains.session.dto.LastReadBookResponse;
import com.projeto_integrado_biblioteca.domains.session.dto.SessionStartRequest;
import com.projeto_integrado_biblioteca.domains.user.User;
import com.projeto_integrado_biblioteca.domains.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionBookRepository sessionBookRepository;
    private final SessionBookMapper sessionBookMapper;
    private final SessionRepository sessionRepository;
    private final UserService userService;
    private final BookService bookService;

    @Transactional
    public void startSession(SessionStartRequest request) {
        Book book = bookService.getBookById(request.bookId());

        if (sessionBookRepository.findByBookId(book.getId()).isEmpty()) {
            User user = userService.getUserById(request.userId());

            Session session = new Session();
            SessionBook sessionBook = new SessionBook(request.totalPages(), book, session);

            session.setUser(user);
            session.setBook(sessionBook);

            sessionRepository.save(session);
        }
    }

    @Transactional(readOnly = true)
    public LastReadBookResponse getUserLastReadBook(Long userId) {
        Session session = this.sessionRepository.findFirstByUserIdAndStatusOrderByBook_LastReadDateDesc(
                userId, SessionStatus.LENDO
        ).orElseThrow(() -> new RuntimeException("Nenhuma leitura ativa encontrada."));

        LastReadBookResponse readBookResponse = this.sessionBookMapper.toLastReadBook(session);

        String key = this.bookService.getBookById(readBookResponse.getId()).getCover().getFileKey();
        readBookResponse.setImageURL(this.bookService.getImageURL(key));

        return readBookResponse;
    }

    public void updateProgress(Long sessionBookId, Integer currentPage) {
        SessionBook book = sessionBookRepository.findById(sessionBookId).orElseThrow(
                () -> new RuntimeException("ERRO ao encontrar o livro de sessão com o id: " + sessionBookId)
        );
        book.updateProgress(currentPage);

        sessionBookRepository.save(book);
    }
}
