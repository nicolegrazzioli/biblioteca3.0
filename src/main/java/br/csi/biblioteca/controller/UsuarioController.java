package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.DadosUsuario;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse; 
import io.swagger.v3.oas.annotations.responses.ApiResponses; 
import io.swagger.v3.oas.annotations.tags.Tag; 
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações de CRUD para Usuários") 
public class UsuarioController {
    private UsuarioService service;
    public UsuarioController(UsuarioService service) {
        this.service = service; 
    }

    @Operation(summary = "Registra um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: email inválido ou nome em branco)")
    })
    @PostMapping("/registrar")
    public ResponseEntity<DadosUsuario> salvar(@Valid @RequestBody Usuario usuario, UriComponentsBuilder uriBuilder){ 
        Usuario usuarioSalvo = service.salvar(usuario);   
        DadosUsuario dadosUsuario = new DadosUsuario(usuarioSalvo);   
        URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuarioSalvo.getIdUs()).toUri(); 
        return ResponseEntity.created(uri).body(dadosUsuario);   
    }
    /*{
  "emailUs": "teste@teste",
  "senhaUs": "123",
  "nomeUs": "teste",
  "ativoUs": true,
  "permissao": "ROLE_USUARIO"
}*/

    @Operation(summary = "Atualiza um usuário existente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Usuário não encontrado\"}")) })
    })
    @PutMapping("/{id}")
    public ResponseEntity<DadosUsuario> atualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario){   
        usuario.setIdUs(id);
        Usuario usuarioAtualizado = service.atualizar(usuario);   
        return ResponseEntity.ok(new DadosUsuario(usuarioAtualizado));   
    }

    @Operation(summary = "Exclui um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível excluir (ex: usuário possui empréstimos)"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Usuário não encontrado\"}")) })
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){   
        service.excluir(id);
        return ResponseEntity.noContent().build(); //status 204   
    }

    @Operation(summary = "Lista todos os usuários ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuários ativos encontrados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<DadosUsuario>> listar() {
        return ResponseEntity.ok(service.listarAtivos()); //ResponseEntity.status(HttpStatus.OK) = 200
    }

    @Operation(summary = "Busca um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ApiErrorDTO.class),
                                    examples = @ExampleObject(value = "{\"mensagem\": \"Usuário não encontrado\"}")) })
    })
    @GetMapping("/{id}")
    public ResponseEntity<DadosUsuario> buscarPorId(@PathVariable Integer id){
        Usuario u = service.getUsuarioById(id);
        return ResponseEntity.ok(new DadosUsuario(u));
    }
}