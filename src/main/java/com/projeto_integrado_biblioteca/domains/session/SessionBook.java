package com.projeto_integrado_biblioteca.domains.session;

import com.projeto_integrado_biblioteca.domains.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_book_sessions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SessionBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "current_page")
    private Integer currentPage;

    private Long progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "session_id", nullable = false, unique = true)
    private Session session;
}
