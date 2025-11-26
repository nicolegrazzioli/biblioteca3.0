package br.csi.biblioteca.model.usuario;

import jakarta.validation.constraints.NotNull;

public record UpdatePermissionDTO(
        @NotNull Boolean ativoUs,
        @NotNull String permissao) {
}
