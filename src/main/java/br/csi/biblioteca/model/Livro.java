package br.csi.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_liv;
    @NonNull
    private String titulo_liv;
    @NonNull
    private String isbn_liv;
    @NonNull
    private int ano_publicacao_liv;
    @NonNull
    private int id_autor_liv;
    @NonNull
    private boolean disponivel_liv;
    @NonNull
    private boolean ativo_liv;
}
