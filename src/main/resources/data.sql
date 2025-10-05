INSERT IGNORE INTO tb_users (user_id, first_name, last_name, email, password, role) VALUES (1, "user", "admin", "admin@gmail.com", "$2a$12$HP6dwIJRJsSWWASyw4r9f.di55zUMJ6Bllk4nodqbOZa4ZSzVaYHy", "ADMIN");
INSERT IGNORE INTO tb_users (user_id, first_name, last_name, email, password, role) VALUES (2, "user", "common", "common@gmail.com", "$2a$12$HP6dwIJRJsSWWASyw4r9f.di55zUMJ6Bllk4nodqbOZa4ZSzVaYHy", "COMMON");


INSERT IGNORE INTO tb_genres(genre_id, name) VALUES (1, "Aventura");
INSERT IGNORE INTO tb_genres(genre_id, name) VALUES (2, "Ação");
INSERT IGNORE INTO tb_genres(genre_id, name) VALUES (3, "Terror");
INSERT IGNORE INTO tb_genres(genre_id, name) VALUES (4, "Sci-fi");
