package com.projeto_integrado_biblioteca.domains.book.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BookFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "file_key", nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private Long size;
}
