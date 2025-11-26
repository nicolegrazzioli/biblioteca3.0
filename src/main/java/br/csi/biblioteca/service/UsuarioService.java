package br.csi.biblioteca.service;

import br.csi.biblioteca.model.usuario.DadosUsuario;
import br.csi.biblioteca.model.usuario.DadosUsuarioCompleto;
import br.csi.biblioteca.model.usuario.UpdatePermissionDTO;
import br.csi.biblioteca.model.usuario.UpdateProfileDTO;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.model.usuario.UsuarioRepository;
import br.csi.biblioteca.service.exception.RecursoNaoEncontradoException;
import br.csi.biblioteca.service.exception.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    // crud
    public Usuario salvar(Usuario usuario) {
        // usuario.setTipoUs("USUARIO");
        usuario.setSenhaUs(new BCryptPasswordEncoder().encode(usuario.getSenhaUs()));
        usuario.setAtivoUs(true);
        usuario.setPermissao("ROLE_USUARIO");
        return repository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        // busca usuario no banco
        Usuario usuarioBanco = this.repository.findById(usuario.getIdUs())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        // atualiza
        usuarioBanco.setNomeUs(usuario.getNomeUs());
        usuarioBanco.setEmailUs(usuario.getEmailUs());
        // usuarioBanco.setSenhaUs(usuario.getSenhaUs());

        return this.repository.save(usuarioBanco);
    }

    public void excluir(Integer id) {
        try {
            this.repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            // se tiver emprestimos ativos
            throw new RegraDeNegocioException("O usuário não pode ser excluído pois tem empréstimos ativos");
        }
    }

    // consulta
    public List<DadosUsuario> listarAtivos() {
        return repository.findByAtivoUsIsTrue().stream().map(DadosUsuario::new).toList();
    }

    public Usuario getUsuarioById(Integer id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }

    // Listar todos os usuários com dados completos (para admin)
    public List<DadosUsuarioCompleto> listarTodosCompleto() {
        return repository.findAll().stream().map(DadosUsuarioCompleto::new).toList();
    }

    // Atualizar apenas permissão e status ativo (para admin)
    public Usuario atualizarPermissao(Integer id, UpdatePermissionDTO dto) {
        Usuario usuario = this.repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setAtivoUs(dto.ativoUs());
        usuario.setPermissao(dto.permissao());

        return this.repository.save(usuario);
    }

    // Atualizar perfil do usuário (nome, email e opcionalmente senha)
    public Usuario atualizarPerfil(Integer id, UpdateProfileDTO dto) {
        Usuario usuario = this.repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        usuario.setNomeUs(dto.nomeUs());
        usuario.setEmailUs(dto.emailUs());

        // Apenas atualiza senha se foi fornecida
        if (dto.senhaUs() != null && !dto.senhaUs().isBlank()) {
            usuario.setSenhaUs(new BCryptPasswordEncoder().encode(dto.senhaUs()));
        }

        return this.repository.save(usuario);
    }
}
