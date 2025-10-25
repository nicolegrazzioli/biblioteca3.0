package br.csi.biblioteca.infra.security;

//tem algumas infos do usuario, armazenado no cliente

import br.csi.biblioteca.model.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenServiceJWT {
    //private final String KEY = "poo2";
    @Value("${jwt.secret}")
    private String KEY;
    public String gerarToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY); //palavra para descriptografar token
            return JWT.create()
                    .withIssuer("API Biblioteca 3.0")
                    .withSubject(user.getUsername())
                    .withClaim("ROLE", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                    .withExpiresAt(dataExpiracao())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(KEY);
            return JWT.require(algorithm)
                    .withIssuer("API Biblioteca 3.0")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }
}
