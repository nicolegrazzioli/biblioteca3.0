package br.csi.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "autor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_aut;
    @NonNull
    private String nome_aut;
    @NonNull
    private String nacionalidade_aut;
    @NonNull
    private String data_nascimento_aut;
}
