package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_session_books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SessionBook {
    private static final Long INITIAL_PROGRESS_VALUE = 0L;
    private static final Integer INITIAL_CURRENT_PAGE = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_page")
    private Integer currentPage = INITIAL_CURRENT_PAGE;

    @Column(name = "total_pages")
    private Integer totalPages;

    private Long progress = INITIAL_PROGRESS_VALUE;

    @Column(name = "last_read_date")
    private LocalDateTime lastReadDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;

    public SessionBook(Integer totalPages, Book book, Session session) {
        this.totalPages = totalPages;
        this.book = book;
        this.session = session;
    }

    public void updateProgress(Integer currentPage) {
        double ratio = currentPage.doubleValue() / getTotalPages().doubleValue();

        this.progress = Math.round(ratio * 100);
        this.currentPage = currentPage;
    }
}
