package br.csi.biblioteca.model.livro;

import br.csi.biblioteca.model.autor.Autor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liv")
    private int idLiv;

    @NotBlank(message = "O livro deve ter um nome")
    @Column(name = "titulo_liv", nullable = false)
    private String tituloLiv;

    @Column(name = "isbn_liv", unique = true)
    private String isbnLiv;

    @Column(name = "ano_publicacao_liv")
    private int anoPublicacaoLiv;

    @Column(name = "disponivel_liv", nullable = false)
    private boolean disponivelLiv;

    @Column(name = "ativo_liv", nullable = false)
    private boolean ativoLiv;

//    @NonNull
//    private int id_autor_liv;
    // mapeia relacionamento com autor
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_autor")
    )
    private Set<Autor> autores;
}
