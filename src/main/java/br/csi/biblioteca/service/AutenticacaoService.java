package br.csi.biblioteca.service;

import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.model.usuario.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AutenticacaoService implements UserDetailsService {
    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = repository.findByEmailUs(login);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario nao encontrado");
        }
        return usuario;
    }
}
