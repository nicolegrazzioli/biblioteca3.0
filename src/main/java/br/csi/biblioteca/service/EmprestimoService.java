package br.csi.biblioteca.service;

import br.csi.biblioteca.controller.EmprestimoDTO;
import br.csi.biblioteca.model.emprestimo.Emprestimo;
import br.csi.biblioteca.model.emprestimo.EmprestimoRepository;
import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.livro.LivroRepository;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.model.usuario.UsuarioRepository;
import br.csi.biblioteca.service.exception.RecursoNaoEncontradoException;
import br.csi.biblioteca.service.exception.RegraDeNegocioException;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
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

    private Usuario getUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // verifica se eh uma instância de usuário
        if (principal instanceof Usuario) {
            return (Usuario) principal;
        }
        throw new RegraDeNegocioException("Usuário não autenticado corretamente");
    }

    //consulta
    public List<Emprestimo> listar(Usuario uLogado) {
        // Checa a permissão real do Spring Security, não um campo de string
        boolean isAdmin = uLogado.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return this.emprestimoRepository.findAll();
        } else {
            return this.emprestimoRepository.findByUsuarioEmp_IdUs(uLogado.getIdUs());
        }
    }


    public Emprestimo buscarPorId(Integer id) {
        return this.emprestimoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado"));
    }

    //criar e devolver
    @Transactional //todas operações no banco são feitas em 1 transação
    public Emprestimo criarEmprestimo(EmprestimoDTO dto) {
        // 1. Buscar as entidades usando os IDs do DTO e os REPOSITÓRIOS
        Livro l = this.livroRepository.findById(dto.getLivroEmp())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Livro não encontrado"));

        Usuario u = this.usuarioRepository.findById(dto.getUsuarioEmp())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        // 2. Aplicar a sua regra de negócio original
        if (!l.isAtivoLiv()) {
            throw new RegraDeNegocioException("O livro não está disponível: está inativo");
        }
        if (!l.isDisponivelLiv()) {
            throw new RegraDeNegocioException("O livro não está disponível: está emprestado");
        }

        // 3. Atualizar o livro
        l.setDisponivelLiv(false);
        // Não é estritamente necessário salvar o 'l' aqui por causa do @Transactional,
        // mas é uma boa prática ser explícito.
        this.livroRepository.save(l);

        // 4. Criar o novo emprestimo
        Emprestimo e = new Emprestimo();
        e.setLivroEmp(l);
        e.setUsuarioEmp(u);
        e.setDataEmprestimoEmp(LocalDate.now());
        e.setDataDevolucaoPrevistaEmp(LocalDate.now().plusDays(14));
        e.setStatusEmp("ATIVO");

        return this.emprestimoRepository.save(e);
    }

    @Transactional
    public Emprestimo renovar(Integer id) {
        Usuario uLogado = getUsuarioAutenticado();
        Emprestimo e = this.emprestimoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado"));

        //verificia se o usuario eh admin ou o dono do emprestimo
        if (!uLogado.getPermissao().equals("ROLE_ADMIN") && uLogado.getIdUs() != e.getUsuarioEmp().getIdUs()) {
            throw new RegraDeNegocioException("Você só pode renovar seus próprios empréstimos");
        }

        if (!e.getStatusEmp().equals("ATIVO")) {
            throw new RegraDeNegocioException("O empréstimo não pode ser renovado pois não está ativo");
        }

        //atualiza data de devolucao prevista
        e.setDataDevolucaoPrevistaEmp(LocalDate.now().plusDays(14));

        return this.emprestimoRepository.save(e);

    }

    @Transactional
    public Emprestimo devolver(Integer id) {
        Usuario uLogado = getUsuarioAutenticado();
        Emprestimo e = this.emprestimoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Empréstimo não encontrado"));

        //verificia se o usuario eh admin ou o dono do emprestimo
        if (!uLogado.getPermissao().equals("ROLE_ADMIN") && uLogado.getIdUs() != e.getUsuarioEmp().getIdUs()) {
            throw new RegraDeNegocioException("Você só pode devolver seus próprios empréstimos");
        }

        if (!e.getStatusEmp().equals("ATIVO")) {
            throw new RegraDeNegocioException("O empréstimo não pode ser devolvido pois não está ativo");
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
