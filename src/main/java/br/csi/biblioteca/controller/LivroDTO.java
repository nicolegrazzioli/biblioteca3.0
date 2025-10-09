package br.csi.biblioteca.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LivroDTO {
    private int idLiv;
    @NotBlank
    private String tituloLiv;
    private String isbnLiv;
    private int anoPublicacaoLiv;
    @NotEmpty
    private Set<Integer> autoresIds;
}
