package br.csi.biblioteca.controller;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO - data transfer object - recebe os dados de requisição de um novo empresitmo
 * dados JSON -> controller
 */
@Getter
@Setter
public class EmprestimoDTO {
    @NotEmpty
    private Integer livroEmp; //idLivro
    @NotEmpty
    private Integer usuarioEmp; //idUsuario
}
