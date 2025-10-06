package br.csi.biblioteca.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByAtivoUsIsTrue();

    //Optional -- pesquisa que pode retornar null
    Optional<Usuario> findByEmailUs(String email);
}
