package com.projeto_integrado_biblioteca.domains.book;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_book_pdf_files")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class BookPdfFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    @Column(name = "pdf_key", nullable = false)
    private String pdfKey;

    @Column(nullable = false)
    private Long size;

    @OneToOne(mappedBy = "pdfFile")
    private Book book;
}
