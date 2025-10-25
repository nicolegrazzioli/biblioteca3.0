package br.csi.biblioteca.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LivroDTO {
    private int idLiv;
    @NotBlank
    private String tituloLiv;
    @NotBlank
    private String isbnLiv;
    @NotNull
    private int anoPublicacaoLiv;
    @NotEmpty
    private Set<Integer> autoresIds;
}
