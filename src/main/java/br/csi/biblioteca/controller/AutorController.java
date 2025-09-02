package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.Autor;
import br.csi.biblioteca.service.AutorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {
    private AutorService service;
    public AutorController(AutorService service) {
        this.service = service;
    }

    @GetMapping("/listar")
    public List<Autor> listar() {
        return this.service.listar();
    }

    @GetMapping("/{id}")
    public Autor autor(@PathVariable Integer id) {
        return this.service.getAutor(id);
    }

    @PostMapping
    public void salvar(@RequestBody Autor autor) {
        this.service.salvar(autor);
    }

    @PutMapping
    public void atualizar(@RequestBody Autor autor) {
        this.service.atualizar(autor);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        this.service.excluir(id);
    }
}
