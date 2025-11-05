package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.emprestimo.Emprestimo;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.EmprestimoService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** status
 * POST = 201 CREATED
 * DELETE = 204 NO CONTENT
 * GET & PUT = 200 OK
 */

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos", description = "Path relacionado aos empréstimos")
public class EmprestimoController {
    private EmprestimoService emprestimoService;
    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    
    /* simula o tipo de usuario pela url, ja que nao tem login explicito
    * ex: http://localhost:8080/biblioteca3.0/emprestimos?idUsuario=1 (admin)
    * ex: http://localhost:8080/biblioteca3.0/emprestimos?idUsuario=2 (usuario)
     */
    @Operation(summary = "Lista os empréstimos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimos encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Emprestimo>> listar(Authentication authentication) {
        Usuario uLogado = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(emprestimoService.listar(uLogado));
    }


    @Operation(summary = "Busca um empréstimo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Empréstimo não encontrado\"}")) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Integer id) {
//        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimoService.buscarPorId(id)); //200
    }

    /*RequestBody - pega o conteudo (json) e coloca numa variavel*/
    @Operation(summary = "Criar um empréstimo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do empréstimo")
    })
    @PostMapping("/registrar")
    public ResponseEntity<Emprestimo> salvar(@Valid @RequestBody EmprestimoDTO emprestimoDTO) {
        // Apenas passe o DTO inteiro para o service
        Emprestimo novo = emprestimoService.criarEmprestimo(emprestimoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo); //201
    }


    @Operation(summary = "Devolver um empréstimo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo devolvido com sucesso"),
            @ApiResponse(responseCode = "400", description = "O empréstimo não pode ser devolvido"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Empréstimo não encontrado\"}")) })
    })
    @PutMapping("{id}/devolver")
    public ResponseEntity<Emprestimo> devolver(@PathVariable Integer id) {
        return ResponseEntity.ok(emprestimoService.devolver(id));
    }


    @Operation(summary = "Renovar um empréstimo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Empréstimo não encontrado\"}")) })
    })
    @PutMapping("{id}/renovar")
    public ResponseEntity<Emprestimo> renovar(@PathVariable Integer id) {
        return ResponseEntity.ok(emprestimoService.renovar(id));
    }

}
