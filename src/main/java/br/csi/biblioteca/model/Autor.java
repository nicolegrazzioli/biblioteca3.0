package br.csi.biblioteca.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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

    public void setData_nascimento_aut(String data_nascimento_aut) {
        try {
            LocalDate date = LocalDate.parse(data_nascimento_aut);
            this.data_nascimento_aut = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de saída inválido. Use AAAA-MM-DD");
        }
    }
}
