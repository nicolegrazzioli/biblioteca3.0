package br.csi.biblioteca.model.autor;

import br.csi.biblioteca.model.livro.Livro;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @JsonIgnore
    @ManyToMany(mappedBy = "autores")
    private Set<Livro> livros;

}
