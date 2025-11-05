package br.csi.biblioteca.controller;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO - data transfer object - recebe os dados de requisição de um novo empresitmo
 * dados JSON -> controller
 */
@Getter
@Setter
public class EmprestimoDTO {
    @NotNull
    @Schema(description = "ID único do livro do empréstimo", example = "2")
    private Integer livroEmp; //idLivro

    @NotNull
    @Schema(description = "ID único do usuário do empréstimo", example = "3")
    private Integer usuarioEmp; //idUsuario
}
