package com.projeto_integrado_biblioteca.domains.download;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DownloadRepository extends JpaRepository<Download, UUID> {

    Optional<Download> findByBookIdAndUserId(Long bookId, Long userId);

    List<Download> findAllByUserId(Long id);
}
