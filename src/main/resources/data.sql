INSERT IGNORE INTO tb_users (id, first_name, last_name, email, password, role) VALUES (1, "user", "admin", "admin@gmail.com", "$2a$12$SNZZ43vrlxmsmOwZj4CYJOp0zoSJYprW16IQmPhhCWCOAZ/CyqyOy", "ADMIN");
INSERT IGNORE INTO tb_users (id, first_name, last_name, email, password, role) VALUES (2, "user", "common", "common@gmail.com", "$2a$12$FhgCpIf/WAh.MPUDhtweKOhLpiqGImFceABSuKmO2xAmfzX.aqPq2", "COMMON");


INSERT IGNORE INTO tb_genres(id, name) VALUES (1, "Aventura");
INSERT IGNORE INTO tb_genres(id, name) VALUES (2, "Ação");
INSERT IGNORE INTO tb_genres(id, name) VALUES (3, "Terror");
INSERT IGNORE INTO tb_genres(id, name) VALUES (4, "Sci-fi");
