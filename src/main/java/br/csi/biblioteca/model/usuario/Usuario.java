package br.csi.biblioteca.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @NotBlank -- strings (valor valido)
 * @NotNull -- não strings
 * @Email
 * @Size(min, max)
 * @Past -- data informada deve ser passado
 */
@Entity(name = "Usuario")
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_us")
    @Schema(description = "ID único do usuário", example = "1")
    private int idUs;

    @Email
    @Column(name = "email_us", unique = true, nullable = false)
    @Schema(description = "Email único do usuário", example = "nome@email.com")
    private String emailUs;

    @NotBlank(message = "Insira uma senha")
    @Column(name = "senha_us", nullable = false)
    @Schema(description = "Senha do usuário", example = "&$%gtS_&")
    private String senhaUs;

    @NotBlank(message = "O usuario deve ter nome")
    @Column(name = "nome_us", nullable = false)
    @Schema(description = "Nome do usuário", example = "Ana Silva")
    private String nomeUs;

    @NotNull
    @Column(name = "ativo_us")
    @Schema(description = "Identifica se o usuário está ativo", example = "true")
    private boolean ativoUs;

    @NotNull
    @Column(name = "permissao")
    @Schema(description = "Identifica o nível de permissão do usuário", example = "USER")
    private String permissao;


    //user details
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.permissao));
    }

    @Override
    public String getPassword() {
        return this.senhaUs;
    }

    @Override
    public String getUsername() {
        return this.emailUs;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.ativoUs;
    }
}
