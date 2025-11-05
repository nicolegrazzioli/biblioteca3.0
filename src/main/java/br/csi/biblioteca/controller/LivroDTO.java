package br.csi.biblioteca.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LivroDTO {
    @Schema(description = "ID único do livro", example = "1")
    private int idLiv;

    @NotBlank
    @Schema(description = "Título do livro", example = "O Hobbit")
    private String tituloLiv;

    @NotBlank
    @Schema(description = "Código ISBN do livro", example = "978-8532511010")
    private String isbnLiv;

    @NotNull
    @Schema(description = "Ano de publicação do livro", example = "1978")
    private int anoPublicacaoLiv;

    @NotEmpty
    @Schema(description = "ID(s) do(s) autor(es) do livro", example = "1, 3")
    private Set<Integer> autoresIds;
}
