package br.csi.biblioteca.model.autor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aut")
    private int idAut;

    @Column(name = "nome_aut", nullable = false)
    private String nomeAut;

    @Column(name = "nacionalidade_aut", nullable = false)
    private String nacionalidadeAut;

    @Column(name = "data_nascimento_aut", nullable = false)
    private java.time.LocalDate dataNascimentoAut;

}
