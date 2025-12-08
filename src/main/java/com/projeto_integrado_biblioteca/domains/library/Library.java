package com.projeto_integrado_biblioteca.domains.library;

import com.projeto_integrado_biblioteca.domains.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_libraries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "library")
    private List<LibraryBook> books = new ArrayList<>();

    @Column(name = "number_of_books")
    private Integer numberOfBooks = 0;

    @OneToOne(mappedBy = "library")
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;


    public void addBook(LibraryBook book) {
        this.books.add(book);
        this.numberOfBooks++;
    }

    public void removeBook(LibraryBook book) {
        this.books.remove(book);
        this.numberOfBooks--;
    }
}
