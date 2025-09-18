package br.csi.biblioteca.model.livro;

import br.csi.biblioteca.model.autor.Autor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_liv")
    private int idLiv;

    @Column(name = "titulo_liv", nullable = false)
    private String tituloLiv;

    @Column(name = "isbn_liv", unique = true, nullable = false)
    private String isbnLiv;

    @Column(name = "ano_publicacao_liv", nullable = false)
    private int anoPublicacaoLiv;

//    @NonNull
//    private int id_autor_liv;
    // mapeia relacionamento com autor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_autor_liv")
    private Autor autorLiv;

    @Column(name = "disponivel_liv", nullable = false)
    private boolean disponivelLiv;

    @Column(name = "ativo_liv", nullable = false)
    private boolean ativoLiv;
}
