package com.projeto_integrado_biblioteca.domains.session;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionBookRepository extends JpaRepository<SessionBook, Long> {

    Optional<SessionBook> findByBookId(Long id);
}
