package br.csi.biblioteca.model.autor;

import br.csi.biblioteca.model.livro.Livro;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.*;
import java.util.Set;

/**
 * @NotBlank -- strings (valor valido)
 * @NotNull -- não strings
 * @Email
 * @Size(min, max)
 * @Past -- data informada deve ser passado
 */
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

    @NotBlank(message = "O nome do autor não pode estar em branco")
    @Column(name = "nome_aut", nullable = false)
    private String nomeAut;

    @Column(name = "nacionalidade_aut")
    private String nacionalidadeAut;

    @Past(message = "O autor já deve ter nascido")
    @Column(name = "data_nascimento_aut")
    private java.time.LocalDate dataNascimentoAut;

    @JsonIgnore
    @ManyToMany(mappedBy = "autores")
    private Set<Livro> livros;

}
