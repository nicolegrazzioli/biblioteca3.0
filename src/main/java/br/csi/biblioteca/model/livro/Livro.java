package br.csi.biblioteca.model.livro;

import br.csi.biblioteca.model.autor.Autor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "ID único do livro", example = "1")
    private int idLiv;

    @NotBlank(message = "O livro deve ter um título")
    @Column(name = "titulo_liv", nullable = false)
    @Schema(description = "Título do livro", example = "O Hobbit")
    private String tituloLiv;

    @Column(name = "isbn_liv", unique = true)
    @Schema(description = "Código ISBN do livro", example = "978-8532511010")
    private String isbnLiv;

    @Column(name = "ano_publicacao_liv")
    @Schema(description = "Ano de publicação do livro", example = "1978")
    private int anoPublicacaoLiv;

    @Column(name = "disponivel_liv", nullable = false)
    @Schema(description = "Identifica se o livro está disponpivel para empréstimos", example = "true")
    private boolean disponivelLiv;

    @Column(name = "ativo_liv", nullable = false)
    @Schema(description = "Identifica se o livro está ativo para empréstimos", example = "true")
    private boolean ativoLiv;

//    private int id_autor_liv;
    // mapeia relacionamento com autor
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "livro_autor",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_autor")
    )
    private Set<Autor> autores;
}
