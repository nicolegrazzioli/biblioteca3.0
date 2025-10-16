package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.DadosAutenticacao;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.TokenServiceJWT;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@AllArgsConstructor
public class AutenticacaoController {
    private final AuthenticationManager manager;
    private final TokenServiceJWT tokenService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        Authentication authentication = manager.authenticate(authenticationToken);

        // Se a autenticação for bem-sucedida, o Spring retorna o objeto UserDetails (nosso Usuario)
        var usuario = (Usuario) authentication.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        return ResponseEntity.ok(token);
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

}
