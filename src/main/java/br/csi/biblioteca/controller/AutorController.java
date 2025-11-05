package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.autor.Autor;
import br.csi.biblioteca.service.AutorService;
import io.swagger.v3.oas.annotations.Operation;
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
//ok
/** status
 * POST = 201 CREATED
 * DELETE = 204 NO CONTENT
 * GET & PUT = 200 OK
 */

@RestController
@RequestMapping("/autores")
@Tag(name = "Autores", description = "Path relacionado aos Autores")
public class AutorController {
    private AutorService service;
    public AutorController(AutorService service) {
        this.service = service;
    }


    @Operation(summary = "Lista os autores")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autores encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
//        List<Autor> autores = service.listar();
        return ResponseEntity.ok(service.listar()); //200
    }


    @Operation(summary = "Busca um autor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Autor não encontrado\"}")) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Integer id) {
//        Autor a = service.getAutor(id);
        return ResponseEntity.ok(service.getAutor(id));
    }


    @Operation(summary = "Criar um autor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do autor")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Autor> salvar(@Valid @RequestBody Autor autor) {
//        Autor a = service.salvar(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(autor));
    }


    @Operation(summary = "Atualizar um autor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do autor"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Autor não encontrado\"}")) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<Autor> atualizar(@PathVariable Integer id, @Valid @RequestBody Autor autor) {
//        Autor a = service.atualizar(autor);
        autor.setIdAut(id);
        return ResponseEntity.ok(service.atualizar(autor));
    }


    @Operation(summary = "Excluir um autor por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "O autor não pode ser excluído"),
            @ApiResponse(responseCode = "204", description = "Autor excluído com sucesso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        service.excluir(id);
        return ResponseEntity.noContent().build(); //204
    }
}
