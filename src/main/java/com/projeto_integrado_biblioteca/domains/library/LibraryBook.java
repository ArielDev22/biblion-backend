package com.projeto_integrado_biblioteca.domains.library;

import com.projeto_integrado_biblioteca.domains.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_library_books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LibraryBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "library_id", referencedColumnName = "id")
    private Library library;
}
