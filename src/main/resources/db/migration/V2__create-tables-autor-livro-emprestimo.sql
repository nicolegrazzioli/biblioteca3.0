-- Tabela de Autores
CREATE TABLE autor (
                       id_aut SERIAL PRIMARY KEY,
                       nome_aut VARCHAR(255) NOT NULL,
                       nacionalidade_aut VARCHAR(100),
                       data_nascimento_aut DATE
);

-- Tabela de Livros
CREATE TABLE livro (
                       id_liv SERIAL PRIMARY KEY,
                       titulo_liv VARCHAR(255) NOT NULL,
                       isbn_liv VARCHAR(50),
                       ano_publicacao_liv INT,
                       id_autor_liv INT,
                       disponivel_liv BOOLEAN DEFAULT true,
                       ativo_liv BOOLEAN DEFAULT true, -- Adicionada a coluna para soft delete
                       CONSTRAINT fk_autor FOREIGN KEY (id_autor_liv) REFERENCES autor(id_aut) ON DELETE RESTRICT -- Corrigida a restrição
);

-- Tabela de Empréstimos
CREATE TABLE emprestimo (
                            id_emp SERIAL PRIMARY KEY,
                            id_livro_emp INT NOT NULL,
                            id_usuario_emp INT NOT NULL,
                            data_emprestimo_emp DATE NOT NULL,
                            data_devolucao_prevista_emp DATE,
                            data_devolucao_efetiva_emp DATE,
                            status_emp VARCHAR(50) CHECK (status_emp IN ('ATIVO', 'CONCLUIDO', 'ATRASADO')),
                            CONSTRAINT fk_livro FOREIGN KEY (id_livro_emp) REFERENCES livro(id_liv),
                            CONSTRAINT fk_usuario FOREIGN KEY (id_usuario_emp) REFERENCES usuario(id_us)
);
