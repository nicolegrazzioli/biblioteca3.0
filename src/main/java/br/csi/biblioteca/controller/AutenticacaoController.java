package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.infra.security.TokenServiceJWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


record DadosAutenticacao(
        @Schema(description = "Email do usuário", example = "admin@email.com") String login,
        @Schema(description = "Senha do usuário", example = "admin123") String senha
) {}
record DadosToken(
        @Schema(description = "Token JWT de autenticação", example = "eyJhbGciOiJIUzI1NiIsInR5cCI...") String token
) {}

@RestController
@RequestMapping("/login")
@AllArgsConstructor
@Tag(name = "Login", description = "Operações de login: autenticação de usuários e geração de token JWT")
public class AutenticacaoController {
    private final AuthenticationManager manager;
    private final TokenServiceJWT tokenService;
    private static final Logger logger = LoggerFactory.getLogger(AutenticacaoController.class);


    @Operation(summary = "Autenticação de usuário", description = "Autentica um usuário com email e senha e retorna um token JWT") //descrição do endpoint
    @ApiResponses(value = { //respostas possiveis que o endpoint pode retornar + descrição
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido - retorna o token JWT",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DadosToken.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas: usuário ou senha incorretos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Usuário ou senha incorretos\"}")
                    )),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiErrorDTO.class),
                            examples = @ExampleObject(value = "{\"mensagem\": \"Erro interno do servidor\"}")))
    })
    @PostMapping
    public ResponseEntity<?> login(@RequestBody DadosAutenticacao dados) {
        System.out.println("--- TENTATIVA DE LOGIN VIA FRONT ---");
        System.out.println("Objeto recebido: " + dados);
        System.out.println("Login recebido: '" + dados.login() + "'");
        System.out.println("Senha recebida: '" + dados.senha() + "'");
        System.out.println("------------------------------------");

        try {

            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            Authentication authentication = manager.authenticate(authenticationToken);

            // Se a autenticação for bem-sucedida, o Spring retorna o objeto UserDetails (Usuario)
            var usuario = (Usuario) authentication.getPrincipal();
            String token = tokenService.gerarToken(usuario);

//            return ResponseEntity.ok(token);
            return ResponseEntity.ok().body(new DadosToken(token));
        } catch (AuthenticationException e) {
            //usuario ou senha invalidos = 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos");
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("Erro interno ao processar login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno de processamento do login"); //500
        }

    }

//    private record DadosAutenticacao(String login, String senha) {}
//    private record DadosToken(String token) {}

}
