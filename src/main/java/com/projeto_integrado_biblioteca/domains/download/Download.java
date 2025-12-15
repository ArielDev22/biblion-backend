package com.projeto_integrado_biblioteca.domains.download;

import com.projeto_integrado_biblioteca.domains.book.models.Book;
import com.projeto_integrado_biblioteca.domains.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tb_downloads")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Download {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "downloaded_at", nullable = false)
    private LocalDate downloadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Download(Book book, User user) {
        this.book = book;
        this.user = user;
        this.downloadedAt = LocalDate.now();
    }

    public void updateDownloadDate(){
        this.setDownloadedAt(LocalDate.now());
    }
}
