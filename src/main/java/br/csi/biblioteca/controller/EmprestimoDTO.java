package br.csi.biblioteca.controller;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO - data transfer object - recebe os dados de requisição de um novo empresitmo
 * dados JSON -> controller
 */
@Getter
@Setter
public class EmprestimoDTO {
    private Integer livroEmp;
    private Integer usuarioEmp;
}
