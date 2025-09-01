CREATE TABLE usuario (
                         id_us SERIAL NOT NULL PRIMARY KEY,
                         nome_us VARCHAR(255) NOT NULL,
                         email_us VARCHAR(255) UNIQUE NOT NULL,
                         senha_us VARCHAR(255) NOT NULL,
                         tipo_us VARCHAR(50) NOT NULL CHECK (tipo_us IN ('ADMIN', 'USUARIO')),
                         ativo_us BOOLEAN DEFAULT true
)
