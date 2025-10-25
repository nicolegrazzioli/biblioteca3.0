package br.csi.biblioteca.model.usuario;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByAtivoUsIsTrue();

    Usuario findByEmailUs(String email);

}
