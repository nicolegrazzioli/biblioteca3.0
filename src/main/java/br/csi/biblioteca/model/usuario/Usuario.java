package br.csi.biblioteca.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_us")
    @Schema(description = "ID único do usuário", example = "1")
    private int idUs;

    @Email
    @Column(name = "email_us", unique = true, nullable = false)
    private String emailUs;

    @NotBlank(message = "Insira uma senha")
    @Column(name = "senha_us", nullable = false)
    private String senhaUs;

    @NotBlank(message = "O usuario deve ter nome")
    @Column(name = "nome_us", nullable = false)
    private String nomeUs;

    @NotNull
    @Column(name = "ativo_us")
    private boolean ativoUs;

    @NotNull
    @Column(name = "tipo_us", nullable = false)
    private String tipoUs;

    private String permissao;
}
