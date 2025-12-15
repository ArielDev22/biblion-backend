package com.projeto_integrado_biblioteca.domains.book.models;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_book_file_pdfs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookFilePDF extends BookFile {


    @OneToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;
}
