package br.csi.biblioteca.model.usuario;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_us")
    private int idUs;

    @Column(name = "email_us", unique = true, nullable = false)
    private String emailUs;

    @Column(name = "senha_us", nullable = false)
    private String senhaUs;

    @Column(name = "nome_us", nullable = false)
    private String nomeUs;

    @Column(name = "ativo_us")
    private boolean ativoUs;

    @Column(name = "tipo_us", nullable = false)
    private String tipoUs;
}
