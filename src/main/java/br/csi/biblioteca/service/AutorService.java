package br.csi.biblioteca.service;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.model.autor.AutorRepository;
import br.csi.biblioteca.service.exception.RecursoNaoEncontradoException;
import br.csi.biblioteca.service.exception.RegraDeNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {
    private final AutorRepository repository;

    public AutorService(AutorRepository repository) {
        this.repository = repository;
    }

    public Autor salvar(Autor autor) {
        return this.repository.save(autor);
    }

    public List<Autor> listar() {
        return this.repository.findAll();
    }

    public Autor getAutor(int id) {
        return this.repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Autor não encontrado"));
    }

    public void excluir(int id) {
        try {
            this.repository.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            throw new RegraDeNegocioException("Erro: o autor não pode ser excluído pois possui livros cadastrados");
        }
    }

    //retorna autor atualizado
    public Autor atualizar(Autor autor) {
        Autor a = this.repository.getReferenceById(autor.getIdAut());
        a.setNomeAut(autor.getNomeAut());
        a.setNacionalidadeAut(autor.getNacionalidadeAut());
        a.setDataNascimentoAut(autor.getDataNascimentoAut());
        return this.repository.save(a);
    }
}
