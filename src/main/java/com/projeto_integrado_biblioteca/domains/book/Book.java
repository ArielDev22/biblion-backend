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

    @Column(name = "image_key", nullable = false)
    private String imageKey;

    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_books_pdfs",
            joinColumns = {@JoinColumn(name = "book_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "pdf_id", referencedColumnName = "id")}
    )
    private BookPdfFile pdfFile;
}
