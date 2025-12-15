package com.projeto_integrado_biblioteca.domains.book.models;

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
@EqualsAndHashCode(of = "id")
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

    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookType type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private BookFilePDF pdf;

    @OneToOne(mappedBy = "book", cascade = CascadeType.ALL)
    private BookFileCover cover;
}
