package br.csi.biblioteca.service;

import br.csi.biblioteca.model.Autor;
import br.csi.biblioteca.model.AutorRepository;
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
        return this.repository.findById(id).orElse(null);
    }

    public void excluir(int id) {
        try {
            this.repository.deleteById(id);
        } catch(DataIntegrityViolationException e) {
            throw new RuntimeException("Erro: o autor não pode ser excluído pois possui livros cadastrados");
        }
    }

    public void atualizar(Autor autor) {
        Autor a = this.repository.getReferenceById(autor.getId_aut());
        a.setNome_aut(autor.getNome_aut());
        a.setNacionalidade_aut(autor.getNacionalidade_aut());
        a.setData_nascimento_aut(autor.getData_nascimento_aut());
        this.repository.save(a);
    }
}
