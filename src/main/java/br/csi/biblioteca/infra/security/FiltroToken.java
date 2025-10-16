package br.csi.biblioteca.infra.security;

import br.csi.biblioteca.model.usuario.UsuarioRepository;
import br.csi.biblioteca.service.AutenticacaoService;
import br.csi.biblioteca.service.TokenServiceJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FiltroToken extends OncePerRequestFilter {
    private final TokenServiceJWT tokenService;
    private final AutenticacaoService autenticacaoService;
    public FiltroToken(TokenServiceJWT tokenService, AutenticacaoService autenticacaoService) {
        this.tokenService = tokenService;
        this.autenticacaoService = autenticacaoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //tenta pegar token do cabecalho da requisicao
        String token = recuperarToken(request);

        if (token != null) { //encontrou
            //valida o token e pega email do usuario
            String subject = this.tokenService.getSubject(token);

            //carregar os detalhes do usuaroi a partir do email
            UserDetails userDetails = this.autenticacaoService.loadUserByUsername(subject);

            //cria autenticacao apra spring security
            var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            //define o usuario como autenticado na requisicao atual
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        //filtros do spring
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null) {
            //remove prefixo Bearer do token
            return header.replace("Bearer ", "");
        }
        return null;
    }

}
