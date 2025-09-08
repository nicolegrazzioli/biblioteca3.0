package br.csi.biblioteca.service;

import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.model.usuario.UsuarioRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario autenticar(String email, String senha){
        Usuario u = this.repository.findByEmailUs(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (u.getSenhaUs().equals(senha)){
            return u;
        } else {
            throw new RuntimeException("Senha incorreta");
        }
    }

    //crud
    public Usuario salvar(Usuario usuario){
        //regra de negocio: sempre do tipo 'usuário'
        usuario.setTipoUs("USUARIO");
        usuario.setAtivoUs(true);
        return repository.save(usuario);
    }

    public Usuario atualizar(Usuario usuario) {
        //busca usuario no banco
        Usuario usuarioBanco = this.repository.findById(usuario.getIdUs()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
            throw new RuntimeException("O usuário não pode ser excluído pois tem empréstimos ativos");
        }
    }

    //consulta
    public List<Usuario> listarAtivos(){
        return repository.findByAtivoUsIsTrue();
    }

    public Usuario getUsuarioById(Integer id){
        return this.repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não enocntrado"));
    }
}
