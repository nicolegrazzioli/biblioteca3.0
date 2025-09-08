package br.csi.biblioteca.service;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.model.autor.AutorRepository;
import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.livro.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroService(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    //consulta
    public List<Livro> listarAll(){
        //retorna todos os livros
        return livroRepository.findAll();
    }

    public List<Livro> listarAtivos(){
        //retorna livros ativos
        return livroRepository.findByAtivoLivIsTrue(); //chama metodo do repository
    }

    public Livro getLivroById(int id){
        return livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
    }


    //edição
    @Transactional
    public Livro salvar(Livro livro){
        //busca autor para ver se existe
        Autor autor = autorRepository.findById(livro.getAutorLiv().getIdAut()).orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        //associa autor ao livro
        livro.setAutorLiv(autor);
        livro.setDisponivelLiv(true);
        livro.setAtivoLiv(true);

        //id nulo/zero --> JPA faz insert
        return livroRepository.save(livro);
    }

    @Transactional
    public Livro atualizar(Livro livro){
        Livro livroBanco = this.livroRepository.findById(livro.getIdLiv()).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        Autor autor = autorRepository.findById(livro.getAutorLiv().getIdAut()).orElseThrow(() -> new RuntimeException("Autor não encontrado"));

        //atualiza livro
        livroBanco.setTituloLiv(livro.getTituloLiv());
        livroBanco.setIsbnLiv(livro.getIsbnLiv());
        livroBanco.setAnoPublicacaoLiv(livro.getAnoPublicacaoLiv());
        livroBanco.setAutorLiv(autor);

        //id existente --> JPA faz update
        return this.livroRepository.save(livroBanco);
    }

    @Transactional
    public void excluir(int id){
        Livro l = this.livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        //regra de negocio
        if (!l.isDisponivelLiv()) {
            throw new RuntimeException("O livro não pode ser excluído pois tem empréstimo ativo");
        }

        //atualiza livro: soft delete
        l.setAtivoLiv(false);
        this.livroRepository.save(l); //update
    }

}
