package br.csi.biblioteca.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

// CORS - cross-origin resource sharing

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AutenticacaoFilter autenticacaoFilter;
    public SecurityConfig(AutenticacaoFilter autenticacaoFilter) {
        this.autenticacaoFilter = autenticacaoFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(crsf -> crsf.disable()) //desabilita alguma coisa = libera endpoint para todos
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                //.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //endpoint de login - todos
                        .requestMatchers(HttpMethod.POST, "/login").permitAll()
                        //endpoint de registro de usuario]
                        .requestMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll()
                        //documentaçao do swagger
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        //deletes - admin
                        .requestMatchers(HttpMethod.DELETE).hasAuthority("ROLE_ADMIN")

                        //post e put em autores e livros - admin
                        .requestMatchers(HttpMethod.POST, "/autores/**", "/livros/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/autores/**", "/livros/**").hasAnyAuthority("ROLE_ADMIN")

                        // usuario autenticado
                        .requestMatchers("/emprestimos/**", "/usuarios/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/autores/**", "/livros/**").authenticated()

                        // nega outras reqs por padrão
                        .anyRequest().denyAll()

                        //outras requisiçoes - usuario autenticado
//                        .anyRequest().authenticated()
                )
                //executar filtro de token antes do filtro de autenticação do spring
//                .addFilterBefore(this.filtroToken, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(this.autenticacaoFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // sessao nao salvs estado do usuario, apenas gera token
    }

    //libera o angular e permite cabeçalhos (token)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); 
        // configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
