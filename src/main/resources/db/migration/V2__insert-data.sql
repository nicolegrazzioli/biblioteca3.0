-- Inserts para a tabela 'usuario'
INSERT INTO usuario (nome_us, email_us, senha_us, tipo_us) VALUES
                                                               ('Admin Geral', 'admin@email.com', 'admin123', 'ADMIN'),
                                                               ('Ana Silva', 'ana.silva@email.com', 'usuario123', 'USUARIO'),
                                                               ('Bruno Costa', 'bruno.costa@email.com', 'usuario123', 'USUARIO'),
                                                               ('Carla Dias', 'carla.dias@email.com', 'usuario123', 'USUARIO'),
                                                               ('Daniel Farias', 'daniel.farias@email.com', 'usuario123', 'USUARIO');

-- Inserts para a tabela 'autor'
INSERT INTO autor (nome_aut, nacionalidade_aut, data_nascimento_aut) VALUES
                                                                         ('J.K. Rowling', 'Britânica', '1965-07-31'),
                                                                         ('George Orwell', 'Britânico', '1903-06-25'),
                                                                         ('Isaac Asimov', 'Russo', '1920-01-02'),
                                                                         ('Neil Gaiman', 'Britânico', '1960-11-10'),
                                                                         ('Terry Pratchett', 'Britânico', '1948-04-28');

-- Inserts para a tabela 'livro'
INSERT INTO livro (titulo_liv, isbn_liv, ano_publicacao_liv) VALUES
                                                                 ('Harry Potter e a Pedra Filosofal', '978-8532511010', 1997),
                                                                 ('1984', '978-8535914849', 1949),
                                                                 ('Fundação', '978-8576572725', 1951),
                                                                 ('Belas Maldições', '978-8595084650', 1990),
                                                                 ('O Hobbit', '978-8595084742', 1937);

-- Inserts para a tabela de junção 'livro_autor'
INSERT INTO livro_autor (id_livro, id_autor) VALUES
                                                 (1, 1), -- Harry Potter -> J.K. Rowling
                                                 (2, 2), -- 1984 -> George Orwell
                                                 (3, 3), -- Fundação -> Isaac Asimov
                                                 (4, 4), -- Belas Maldições -> Neil Gaiman
                                                 (4, 5), -- Belas Maldições -> Terry Pratchett
                                                 (5, 1); -- O Hobbit também pode ter sido "editado" por J.K. Rowling


-- Inserts para a tabela 'emprestimo'
INSERT INTO emprestimo (id_livro_emp, id_usuario_emp, data_emprestimo_emp, data_devolucao_prevista_emp, status_emp) VALUES
                                                (1, 2, '2025-09-10', '2025-09-24', 'ATIVO'), -- Ana Silva pegou Harry Potter
                                                (2, 3, '2025-09-01', '2025-09-15', 'ATRASADO'), -- Bruno Costa pegou 1984 e está atrasado
                                                (3, 4, '2025-08-20', '2025-09-03', 'CONCLUIDO'), -- Carla Dias pegou Fundação e já devolveu
                                                (5, 2, '2025-09-15', '2025-09-29', 'ATIVO'), -- Ana Silva também pegou O Hobbit
                                                (4, 5, '2025-09-18', '2025-10-02', 'ATIVO'); -- Daniel Farias pegou Belas Maldições
