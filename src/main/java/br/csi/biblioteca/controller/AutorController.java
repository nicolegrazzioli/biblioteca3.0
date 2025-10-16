package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.service.AutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//ok
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
//        List<Autor> autores = service.listar();
        return ResponseEntity.ok(service.listar()); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Integer id) {
//        Autor a = service.getAutor(id);
        return ResponseEntity.ok(service.getAutor(id));
    }

    @PostMapping("/registrar")
    public ResponseEntity<Autor> salvar(@Valid @RequestBody Autor autor) {
//        Autor a = service.salvar(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(autor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
//        Autor a = service.atualizar(autor);
        autor.setIdAut(id);
        return ResponseEntity.ok(service.atualizar(autor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }
}
