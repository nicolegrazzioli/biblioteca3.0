package br.csi.biblioteca.model.livro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Integer> {
    //buscar livros ativos
    List<Livro> findByAtivoLivIsTrue();
}
