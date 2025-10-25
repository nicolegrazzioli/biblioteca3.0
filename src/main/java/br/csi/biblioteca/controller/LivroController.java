package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
@Tag(name = "Livros", description = "Operações de CRUD para Livros")
public class LivroController {
    private LivroService livroService;
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @Operation(summary = "Lista todos os livros ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livros ativos encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Livro>> listarAtivos() {
        return ResponseEntity.ok(livroService.listarAtivos()); //200
    }

    @Operation(summary = "Lista todos os livros (incluindo inativos)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos os livros encontrados com sucesso")
    })
    @GetMapping("/all")
    public ResponseEntity<List<Livro>> listarAll() {
        return ResponseEntity.ok(livroService.listarAll()); //200
    }

    @Operation(summary = "Busca um livro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable Integer id) {
        return ResponseEntity.ok(livroService.getLivroById(id)); //200
    }

    @Operation(summary = "Registra um novo livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: sem autor ou título em branco)")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Livro> salvar(@Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.salvar(livroDTO)); //201
    }

    @Operation(summary = "Atualiza um livro existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: sem autor)"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Integer id, @Valid @RequestBody LivroDTO livroDTO){
        livroDTO.setIdLiv(id);
        return ResponseEntity.ok(livroService.atualizar(livroDTO));
    }

    @Operation(summary = "Exclui (soft delete) um livro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro excluído (inativado) com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível excluir (ex: livro está emprestado)"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){
        livroService.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }
}