package br.csi.biblioteca.service;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.model.emprestimo.Emprestimo;
import br.csi.biblioteca.model.emprestimo.EmprestimoRepository;
import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.livro.LivroRepository;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.model.usuario.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * contem as regras de negocio, implementa e faz o registro do emprestimo em si
 * verificar se o livro está disponivel
 * define data atual como inicio do emprestimo
 * calcula data final (+14d)
 * define livro como indisponivel
 * define status como ativo - novo objeto Emprestimo
 */

@Service
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioRepository usuarioRepository;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroRepository, UsuarioRepository usuarioRepository) {
        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //consulta
    public List<Emprestimo> listar(Integer idUsuarioLogado) {
        Usuario uLogado = usuarioRepository.findById(idUsuarioLogado).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if (uLogado.getTipoUs().equals("ADMIN")) {
            //se for ADMIN, ve todos emprestimos de todos os usuarios
            return this.emprestimoRepository.findAll();
        } else {
            //se for USUARIO, ve so os seus emprestimos
            return this.emprestimoRepository.findByUsuarioEmp_IdUs(idUsuarioLogado);
        }
    }

//    public List<Emprestimo> listarPorUsuario(Integer idUsuario) {
//        return this.emprestimoRepository.findByUsuarioEmp_IdUs(idUsuario);
//    }

    public Emprestimo buscarPorId(Integer id) {
        return this.emprestimoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
    }

    //criar e devolver
    @Transactional //todas operações no banco são feitas em 1 transação
    public Emprestimo criarEmprestimo(Integer idLivro, Integer idUsuario) {
        Livro livro = this.livroRepository.findById(idLivro).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        Usuario usuario = this.usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        //regra de negocio
        if (!livro.isAtivoLiv()) {
            throw new RuntimeException("O livro não está disponível: está inativo");
        }
        if (!livro.isDisponivelLiv()) {
            throw new RuntimeException("O livro não está disponível: está emprestado");
        }

        livro.setDisponivelLiv(false);
        this.livroRepository.save(livro); //salva alteração

        //novo emprestimo
        Emprestimo e = new  Emprestimo();
        e.setLivroEmp(livro);
        e.setUsuarioEmp(usuario);
        e.setDataEmprestimoEmp(LocalDate.now());
        e.setDataDevolucaoPrevistaEmp(LocalDate.now().plusDays(14));
        e.setStatusEmp("ATIVO");

        return this.emprestimoRepository.save(e);
    }

    @Transactional
    public Emprestimo devolver(Integer id) {
        Emprestimo e = this.emprestimoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empréstimo não encontrado"));
        if (!e.getStatusEmp().equals("ATIVO")) {
            throw new RuntimeException("O empréstimo não pode ser devolvido pois não está ativo");
        }

        //atualiza estado do livro
        Livro l =  e.getLivroEmp();
        l.setDisponivelLiv(true);
        this.livroRepository.save(l); //salva estado

        //atualiza estado do emprestimo
        e.setStatusEmp("CONCLUIDO");
        e.setDataDevolucaoEfetivaEmp(LocalDate.now());

        return this.emprestimoRepository.save(e);
    }
}
