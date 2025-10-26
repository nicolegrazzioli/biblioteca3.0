package br.csi.biblioteca.service;

import br.csi.biblioteca.model.usuario.DadosUsuario;
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

    //crud
    public Usuario salvar(Usuario usuario){
        //usuario.setTipoUs("USUARIO");
        usuario.setSenhaUs(new BCryptPasswordEncoder().encode(usuario.getSenhaUs()));
        usuario.setAtivoUs(true);
        usuario.setPermissao("ROLE_USUARIO");
        return repository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        //busca usuario no banco
        Usuario usuarioBanco = this.repository.findById(usuario.getIdUs()).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        //atualiza
        usuarioBanco.setNomeUs(usuario.getNomeUs());
        usuarioBanco.setEmailUs(usuario.getEmailUs());
        // usuarioBanco.setSenhaUs(usuario.getSenhaUs());

        return this.repository.save(usuarioBanco);
    }

    public void excluir(Integer id){
        try {
            this.repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            //se tiver emprestimos ativos
            throw new RegraDeNegocioException("O usuário não pode ser excluído pois tem empréstimos ativos");
        }
    }

    //consulta
    public List<DadosUsuario> listarAtivos(){
        return repository.findByAtivoUsIsTrue().stream().map(DadosUsuario::new).toList();
    }

    public Usuario getUsuarioById(Integer id){
        return this.repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    }
}
