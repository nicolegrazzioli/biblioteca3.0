package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.service.LivroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
            @ApiResponse(responseCode = "200", description = "Lista de livros ativos retornads com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Livro.class)),
                            examples = @ExampleObject(
                                    name = "Exemplo de Lista de Livros",
                                    value = "[{\"idLiv\":1,\"tituloLiv\":\"O Hobbit\",\"isbnLiv\":\"978-8532511010\",\"anoPublicacaoLiv\":1978,\"disponivelLiv\":true,\"ativoLiv\":true,\"autores\":[{\"idAut\":1,\"nomeAut\":\"J.K. Rowling\",\"nacionalidadeAut\":\"Brasileira\",\"dataNascimentoAut\":\"2025-11-05\"}]}," +
                                            "{\"idLiv\":2,\"tituloLiv\":\"1984\",\"isbnLiv\":\"978-8535914849\",\"anoPublicacaoLiv\":1949,\"disponivelLiv\":true,\"ativoLiv\":true,\"autores\":[{\"idAut\":2,\"nomeAut\":\"George Orwell\",\"nacionalidadeAut\":\"Britânico\",\"dataNascimentoAut\":\"1903-06-25\"}]}]"
                            )
                    )),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer autenticação)",
                    content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"mensagem\": \"Acesso negado\"}")
            )),
    })
    @GetMapping
    public ResponseEntity<List<Livro>> listarAtivos() {
        return ResponseEntity.ok(livroService.listarAtivos()); //200
    }



    @Operation(summary = "Lista todos os livros (incluindo inativos)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista completa de livros retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Livro.class)),
                            examples = @ExampleObject(
                                    name = "Exemplo de Lista de Livros",
                                    value = "[{\"idLiv\":1,\"tituloLiv\":\"O Hobbit\",\"isbnLiv\":\"978-8532511010\",\"anoPublicacaoLiv\":1978,\"disponivelLiv\":true,\"ativoLiv\":false,\"autores\":[{\"idAut\":1,\"nomeAut\":\"J.K. Rowling\",\"nacionalidadeAut\":\"Brasileira\",\"dataNascimentoAut\":\"2025-11-05\"}]}," +
                                            "{\"idLiv\":2,\"tituloLiv\":\"1984\",\"isbnLiv\":\"978-8535914849\",\"anoPublicacaoLiv\":1949,\"disponivelLiv\":true,\"ativoLiv\":true,\"autores\":[{\"idAut\":2,\"nomeAut\":\"George Orwell\",\"nacionalidadeAut\":\"Britânico\",\"dataNascimentoAut\":\"1903-06-25\"}]}]"
                            )
                    )
            ),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer autenticação)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"mensagem\": \"Acesso negado\"}")
                    )),
    })
    @GetMapping("/all")
    public ResponseEntity<List<Livro>> listarAll() {
        return ResponseEntity.ok(livroService.listarAll()); //200
    }



    @Operation(summary = "Busca um livro por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Livro encontrado com sucesso",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(implementation = Livro.class)) }),
        @ApiResponse(responseCode = "404", description = "Livro não encontrado",
            content = {
                @Content(mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorDTO.class),
                examples = @ExampleObject(value = "{\"mensagem\": \"Livro não encontrado\"}")) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Livro> getLivroById(@PathVariable Integer id) {
        return ResponseEntity.ok(livroService.getLivroById(id)); //200
    }



    @Operation(summary = "Registra um novo livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class))),

            @ApiResponse(responseCode = "400", description = "Dados inválidos (erro de validação ou regra de negócio)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {ApiErrorDTO.class, Map.class}),
                            examples = {
                                    @ExampleObject(name = "Erro de Regra", value = "{\"mensagem\": \"O livro deve ter pelo menos um autor\"}"),
                                    @ExampleObject(name = "Erro de Validação", value = "{\"tituloLiv\": \"O livro deve ter um nome\"}")
                            })),

            @ApiResponse(responseCode = "403", description = "Acesso negado (requer permissão de ADMIN)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"mensagem\": \"Acesso negado\"}")
                    )),
    })
    @PostMapping("/registrar")
    public ResponseEntity<Livro> salvar(@Valid @RequestBody LivroDTO livroDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(livroService.salvar(livroDTO)); //201
    }



    @Operation(summary = "Atualiza um livro existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Livro.class))),

            @ApiResponse(responseCode = "400", description = "Dados inválidos (erro de validação ou regra de negócio)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {ApiErrorDTO.class, Map.class}),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Dados inválidos\"}")
                    )),

            @ApiResponse(responseCode = "403", description = "Acesso negado (requer permissão de ADMIN)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"mensagem\": \"Acesso negado\"}")
                    )),

            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Livro não encontrado\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Integer id, @Valid @RequestBody LivroDTO livroDTO){
        livroDTO.setIdLiv(id);
        return ResponseEntity.ok(livroService.atualizar(livroDTO));
    }



    @Operation(summary = "Exclui (soft delete) um livro por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro excluído (inativado) com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de regra de negócio",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"O livro não pode ser excluído pois tem empréstimo ativo\"}"))),
            @ApiResponse(responseCode = "403", description = "Acesso negado (requer permissão de ADMIN)",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"mensagem\": \"Acesso negado\"}")
                    )),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Livro não encontrado\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){
        livroService.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }
}