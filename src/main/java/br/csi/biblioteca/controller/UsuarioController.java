package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.DadosUsuario;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Operações de CRUD para Usuários")
public class UsuarioController {
    private final UsuarioService service;
    public UsuarioController(UsuarioService service) {
        this.service = service; 
    }

    @Operation(summary = "Registra um novo usuário com permissão 'ROLE_USUARIO' (default)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {ApiErrorDTO.class, Map.class}),
                            examples = {
                                    @ExampleObject(name = "Erro de Regra", value = "{\"mensagem\": \"Email já cadastrado\"}"),
                                    @ExampleObject(name = "Erro de Validação", value = "{\"emailUs\": \"E-mail inválido\", \"nomeUs\": \"O usuario deve ter nome\"}")
                            })),
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
        @ApiResponse(responseCode = "200", description = "Usuario atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Usuario.class))),

            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {ApiErrorDTO.class, Map.class}),
                            examples = {
                                    @ExampleObject(name = "Erro de Validação", value = "{\"emailUs\": \"E-mail inválido\"}")
                            })),

        @ApiResponse(responseCode = "404", description = "Usuario não encontrado",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ApiErrorDTO.class),
                        examples = @ExampleObject(value = "{\"mensagem\": \"Usuario não encontrado\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<DadosUsuario> atualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario){   
        usuario.setIdUs(id);
        Usuario usuarioAtualizado = service.atualizar(usuario);   
        return ResponseEntity.ok(new DadosUsuario(usuarioAtualizado));   
    }



    @Operation(summary = "Exclui um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Não é possível excluir usuário",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(oneOf = {ApiErrorDTO.class, Map.class}),
                            examples = {
                                    @ExampleObject(name = "Erro de Regra", value = "{\"mensagem\": \"O usuário possui empréstimos ativos\"}")
                            })),

            @ApiResponse(responseCode = "404", description = "Usuario não encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Usuario não encontrado\"}")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){   
        service.excluir(id);
        return ResponseEntity.noContent().build(); //status 204   
    }



    @Operation(summary = "Lista todos os usuários ativos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários ativos retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DadosUsuario.class)),
                            examples = @ExampleObject(
                                    name = "Exemplo de Lista de Usuários",
                                    value = "[{\"id\":2,\"email\":\"ana.silva@email.com\",\"permissao\":\"ROLE_USUARIO\"}," +
                                            "{\"id\":3,\"email\":\"bruno.costa@email.com\",\"permissao\":\"ROLE_USUARIO\"}]"
                            )
                    )
            ),
    })
    @GetMapping
    public ResponseEntity<List<DadosUsuario>> listar() {
        return ResponseEntity.ok(service.listarAtivos()); //ResponseEntity.status(HttpStatus.OK) = 200
    }



    @Operation(summary = "Busca um usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DadosUsuario.class)) }),
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