package br.csi.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_us;
    @NonNull
    private String email_us;
    @NonNull
    private String senha_us;
    @NonNull
    private String nome_us;
    @NonNull
    private boolean ativo_us;
    @NonNull
    private String tipo_us;

}
