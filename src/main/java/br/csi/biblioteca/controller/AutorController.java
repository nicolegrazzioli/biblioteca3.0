package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.service.AutorService;
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
@RequestMapping("/autores")
public class AutorController {
    private AutorService service;
    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        List<Autor> autores = service.listar();
        return ResponseEntity.ok(autores); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Integer id) {
        Autor a = service.getAutor(id);
        return ResponseEntity.ok(a);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Autor> salvar(@RequestBody Autor autor) {
        Autor a = service.salvar(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@RequestBody Autor autor) {
        Autor a = service.atualizar(autor);
        return ResponseEntity.ok(a);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }
}
