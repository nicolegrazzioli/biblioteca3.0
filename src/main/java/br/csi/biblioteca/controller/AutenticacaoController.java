package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.infra.security.TokenServiceJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
@Tag(name = "Login", description = "Path relacionado a operações de login")
public class AutenticacaoController {
    private final AuthenticationManager manager;
    private final TokenServiceJWT tokenService;

    @Operation(summary = "Fazer login", description = "Default") //descrição do endpoint
    @ApiResponses(value = { //respostas possiveis que o endpoint pode retornar + descrição
            @ApiResponse(responseCode = "200", description = "Login bem sucedido",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DadosToken.class))}), //tipo de retorno e formato do objeto
            @ApiResponse(responseCode = "401", description = "Usuário ou senha incorretos"),
            @ApiResponse(responseCode = "500", description = "Erro interno de processamento do login")
    })
    @PostMapping
    public ResponseEntity<?> login(@RequestBody DadosAutenticacao dados) {
        try {

            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            Authentication authentication = manager.authenticate(authenticationToken);

            // Se a autenticação for bem-sucedida, o Spring retorna o objeto UserDetails (nosso Usuario)
            var usuario = (Usuario) authentication.getPrincipal();
            String token = tokenService.gerarToken(usuario);

//            return ResponseEntity.ok(token);
            return ResponseEntity.ok().body(new DadosToken(token));
        } catch (AuthenticationException e) {
            //usuario ou senha invalidos = 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno de processamento do login"); //500
        }

//        try {
//            Authentication autenticado = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
//            //regitra no manager
//            Authentication at = manager.authenticate(autenticado);
//
//            User user = (User) at.getPrincipal();
//            String token = tokenService.gerarToken(user);
//
//            return ResponseEntity.ok().body(token);
//
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
    }

    private record DadosAutenticacao(String login, String senha) {}
    private record DadosToken(String token) {}

}
