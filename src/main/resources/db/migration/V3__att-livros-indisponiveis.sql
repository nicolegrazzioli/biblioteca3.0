UPDATE livro 
SET disponivel_liv = false 
WHERE id_liv IN (
    SELECT id_livro_emp FROM emprestimo WHERE status_emp IN ('ATIVO', 'ATRASADO')
);