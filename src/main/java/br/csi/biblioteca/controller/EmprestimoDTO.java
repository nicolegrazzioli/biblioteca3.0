package br.csi.biblioteca.controller;
import jakarta.validation.constraints.NotEmpty;
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
    private Integer livroEmp; //idLivro
    @NotNull
    private Integer usuarioEmp; //idUsuario
}
