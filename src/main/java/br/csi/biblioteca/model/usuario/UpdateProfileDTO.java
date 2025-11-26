package br.csi.biblioteca.model.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateProfileDTO(
        @NotBlank(message = "O usuario deve ter nome") String nomeUs,

        @Email(message = "Email inválido") String emailUs,

        String senhaUs // opcional - apenas se o usuário quiser alterar
) {
}
