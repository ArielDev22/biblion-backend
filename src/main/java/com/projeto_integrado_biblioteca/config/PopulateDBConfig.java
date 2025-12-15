package com.projeto_integrado_biblioteca.config;

import com.projeto_integrado_biblioteca.domains.book.models.Book;
import com.projeto_integrado_biblioteca.domains.book.BookRepository;
import com.projeto_integrado_biblioteca.domains.book.models.BookFileCover;
import com.projeto_integrado_biblioteca.domains.book.models.BookType;
import com.projeto_integrado_biblioteca.domains.genre.Genre;
import com.projeto_integrado_biblioteca.domains.genre.GenreRepository;
import com.projeto_integrado_biblioteca.domains.library.Library;
import com.projeto_integrado_biblioteca.domains.storage.StorageService;
import com.projeto_integrado_biblioteca.domains.user.User;
import com.projeto_integrado_biblioteca.domains.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class PopulateDBConfig {
    private final JdbcTemplate jdbcTemplate;

    public PopulateDBConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    CommandLineRunner registerBooks(
            BookRepository bookRepository,
            GenreRepository genreRepository,
            StorageService storageService,
            UserRepository userRepository
    ) {
        return args -> {
            if (!isTablePopulated()) {
                System.out.println("Preenchendo o banco de dados...");

                User user1 = userRepository.findById(1L).get();
                User user2 = userRepository.findById(2L).get();

                user1.setLibrary(new Library());
                user2.setLibrary(new Library());

                userRepository.save(user1);
                userRepository.save(user2);


                List<Genre> savedGenres = genreRepository.saveAll(
                        List.of(
                                new Genre(null, "Aventura", new HashSet<>()),
                                new Genre(null, "Ação", new HashSet<>()),
                                new Genre(null, "Fantasia", new HashSet<>()),
                                new Genre(null, "Ficção", new HashSet<>()),
                                new Genre(null, "Romance", new HashSet<>())
                        )
                );

                Map<String, Genre> genreMap = savedGenres.stream().collect(Collectors.toMap(Genre::getName, g -> g));

                String imagesPath = "/static/images/";

                List<Book> books = List.of(
                        new Book(null, "5647375698675", "1984", "George Orwell", null, null, BookType.FREE, Set.of(genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698674", "A Revolução dos Bichos", "George Orwell", null, null, BookType.FREE, Set.of(genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698671", "Admirável Mundo Novo", " Aldous Huxley", null, 1932, BookType.FREE, Set.of(genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698672", "O Apanhador no Campo de Centeio", "J. D. Salinger", null, 1945, BookType.FREE, Set.of(genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698670", "As Crônicas de Nárnia", "C. S. Lewis", null, null, BookType.FREE, Set.of(genreMap.get("Aventura"), genreMap.get("Fantasia")), null, null),
                        new Book(null, "5647375698679", "A Jornada do Jovem Mago", "J. K. Rowling", null, null, BookType.FREE, Set.of(genreMap.get("Aventura"), genreMap.get("Fantasia")), null, null),
                        new Book(null, "5647375698678", "Jane Eyre", "Charlotte Brontë", null, 1847, BookType.FREE, Set.of(genreMap.get("Romance")), null, null),
                        new Book(null, "5647375698677", "O Senhor dos Anéis", "J. R. R. Tolkien", null, 1954, BookType.FREE, Set.of(genreMap.get("Aventura"), genreMap.get("Fantasia"), genreMap.get("Ação")), null, null),
                        new Book(null, "5647375698676", "Orgulho e Preconceito", "Jane Austen", null, 1813, BookType.FREE, Set.of(genreMap.get("Romance"), genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698688", "O Hobbit", "J. R. R. Tolkien", null, 1937, BookType.FREE, Set.of(genreMap.get("Aventura"), genreMap.get("Fantasia"), genreMap.get("Ação")), null, null),
                        new Book(null, "5647375698684", "Duna", "Frank Herbert", null, 1965, BookType.FREE, Set.of(genreMap.get("Romance"), genreMap.get("Ficção")), null, null),
                        new Book(null, "5647375698682", "O Sol Nasce Para Todos", "Harper Lee", null, 1960, BookType.FREE, Set.of(genreMap.get("Romance")), null, null)
                );

                List<Book> savedBooks = bookRepository.saveAll(books);

                for (Book b : savedBooks) {
                    String imageFilename = b.getTitle().toLowerCase().replace(" ", "-") + ".jpg";
                    String fullPath = imagesPath.concat(imageFilename);

                    try (InputStream inputStream = getClass().getResourceAsStream(fullPath)) {
                        if (inputStream == null) {
                            throw new RuntimeException("Imagem não encontrada: " + imageFilename);
                        }

                        byte[] imageBytes = inputStream.readAllBytes();

                        long size = imageBytes.length;

                        String key = storageService.uploadImageAsStream(new ByteArrayInputStream(imageBytes));

                        BookFileCover cover = new BookFileCover(b);
                        cover.setFileKey(key);
                        cover.setFilename(imageFilename);
                        cover.setSize(size);


                        b.setCover(cover);
                    } catch (IOException e) {
                        throw new RuntimeException("Erro ao ler arquivo: " + imageFilename, e);
                    }
                }
                bookRepository.saveAll(savedBooks);
            }
        };
    }

    private boolean isTablePopulated() {
        String table = "tb_books";

        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table.toLowerCase(), Integer.class);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            System.out.println("Tabela '" + table.toLowerCase() + "' não encontrada.");
            return false;
        }
    }
}
