package com.projeto_integrado_biblioteca.domains.book;

import com.projeto_integrado_biblioteca.domains.genre.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_books")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "bookId")
@NamedEntityGraph(
        name = "Book.withGenres",
        attributeNodes = @NamedAttributeNode("genres")
)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    private String description;
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType type;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "tb_books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @Column(name = "cover_path", nullable = false)
    private String coverPath;

    @Column(name = "pdf_key")
    private String pdfKey;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "copies_available")
    private Integer copiesAvailable;

    @Column(name = "copies_on_loan")
    private Integer copiesOnLoan;
}
