package com.projeto_integrado_biblioteca.domains.book;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByTitle(String title);

    Optional<Book> findByIsbn(String isbn);

    @EntityGraph(value = "Book.withGenres", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findByIdWithGenres(@Param("id") Long id);
}
