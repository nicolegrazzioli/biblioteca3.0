CREATE TABLE usuario (
                         id_us SERIAL NOT NULL PRIMARY KEY,
                         nome_us VARCHAR(255) NOT NULL,
                         email_us VARCHAR(255) UNIQUE NOT NULL,
                         senha_us VARCHAR(255) NOT NULL,
                         tipo_us VARCHAR(50) NOT NULL CHECK (tipo_us IN ('ADMIN', 'USUARIO')),
                         ativo_us BOOLEAN DEFAULT true
);

CREATE TABLE autor (
                       id_aut SERIAL PRIMARY KEY,
                       nome_aut VARCHAR(255) NOT NULL,
                       nacionalidade_aut VARCHAR(100),
                       data_nascimento_aut DATE
);

CREATE TABLE livro (
                       id_liv SERIAL PRIMARY KEY,
                       titulo_liv VARCHAR(255) NOT NULL,
                       isbn_liv VARCHAR(50),
                       ano_publicacao_liv INT,
                       disponivel_liv BOOLEAN DEFAULT true,
                       ativo_liv BOOLEAN DEFAULT true -- soft delete
);

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

CREATE TABLE livro_autor (
                         id_livro INT NOT NULL,
                         id_autor INT NOT NULL,
                         CONSTRAINT fk_livro FOREIGN KEY (id_livro) REFERENCES livro(id_liv),
                         CONSTRAINT fk_autor FOREIGN KEY (id_autor) REFERENCES autor(id_aut),
                         PRIMARY KEY (id_livro, id_autor)
);
