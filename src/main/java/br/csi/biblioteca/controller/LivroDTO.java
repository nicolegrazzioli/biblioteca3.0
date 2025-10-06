package br.csi.biblioteca.controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LivroDTO {
    private int idLiv;
    private String tituloLiv;
    private String isbnLiv;
    private int anoPublicacaoLiv;
    private Set<Integer> autoresIds;
}
