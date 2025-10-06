package br.csi.biblioteca.service;

import br.csi.biblioteca.controller.LivroDTO;
import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.model.autor.AutorRepository;
import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.livro.LivroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public Livro salvar(LivroDTO livroDTO){
        //busca autor(es)
        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(livroDTO.getAutoresIds()));
        if(autores.isEmpty()){
            throw new RuntimeException("O livro deve ter pelo menos um autor");
        }

        Livro livro = new Livro();
        livro.setTituloLiv(livroDTO.getTituloLiv());
        livro.setIsbnLiv(livroDTO.getIsbnLiv());
        livro.setAnoPublicacaoLiv(livroDTO.getAnoPublicacaoLiv());
        livro.setAutores(autores);
        livro.setDisponivelLiv(true);
        livro.setAtivoLiv(true);

        //id nulo/zero --> JPA faz insert
        return livroRepository.save(livro);
    }

    @Transactional
    public Livro atualizar(LivroDTO livroDTO){
        Livro livroBanco = livroRepository.findById(livroDTO.getIdLiv()).orElseThrow(() -> new RuntimeException("Livro não encontrado"));
        Set<Autor> autores = new HashSet<>(autorRepository.findAllById(livroDTO.getAutoresIds()));
        if(autores.isEmpty()){
            throw new RuntimeException("O livro deve ter pelo menos um autor");
        }

        //atualiza livro
        livroBanco.setTituloLiv(livroDTO.getTituloLiv());
        livroBanco.setIsbnLiv(livroDTO.getIsbnLiv());
        livroBanco.setAnoPublicacaoLiv(livroDTO.getAnoPublicacaoLiv());
        livroBanco.setAutores(autores);

        //id existente --> JPA faz update
        return livroRepository.save(livroBanco);
    }

    @Transactional
    public void excluir(int id){
        Livro l = this.livroRepository.findById(id).orElseThrow(() -> new RuntimeException("Livro não encontrado"));

        //regra de negocio
        if (!l.isDisponivelLiv()) { //se o livro nao esta disponivel
            throw new RuntimeException("O livro não pode ser excluído pois tem empréstimo ativo");
        }

        //atualiza livro: soft delete
        l.setAtivoLiv(false);
        this.livroRepository.save(l); //update
    }

}
