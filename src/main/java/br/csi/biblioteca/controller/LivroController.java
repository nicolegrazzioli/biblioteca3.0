package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.AutorService;
import br.csi.biblioteca.service.LivroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** status
 * POST = 201 CREATED
 * DELETE = 204 NO CONTENT
 * GET & PUT = 200 OK
 */

@RestController
@RequestMapping("/livros")
public class LivroController {
    private LivroService livroService;
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    //listar livros ativos - default
    @GetMapping
    public ResponseEntity<List<Livro>> listarAtivos() {
//        List<Livro> livrosAtivos = livroService.listarAtivos();
        return ResponseEntity.ok(livroService.listarAtivos()); //200
    }

    //listar todos os livros, inlcuindo desativados
    @GetMapping("/all")
    public ResponseEntity<List<Livro>> listarAll() {
//        List<Livro> livrosAll = livroService.listarAll();
        return ResponseEntity.ok(livroService.listarAll()); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable Integer id) {
//        Livro livro = livroService.getLivroById(id);
        return ResponseEntity.ok(livroService.getLivroById(id)); //200
    }

    @PostMapping("/registrar")
    public ResponseEntity<Livro> salvar(@RequestBody LivroDTO livroDTO) {
//        Livro novo =  livroService.salvar(livro);
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.salvar(livroDTO)); //201
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Integer id, @RequestBody LivroDTO livroDTO){
        livroDTO.setIdLiv(id);
//        Livro l =  livroService.atualizar(livro);
        return ResponseEntity.ok(livroService.atualizar(livroDTO)); //200
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Livro> excluir(@PathVariable Integer id){
        livroService.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }


}
