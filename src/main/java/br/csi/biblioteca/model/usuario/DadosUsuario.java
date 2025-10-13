package br.csi.biblioteca.model.usuario;

public record DadosUsuario(int id, String email, String permissao) {
    public DadosUsuario(Usuario usuario) {
        this(usuario.getIdUs(), usuario.getEmailUs(), usuario.getPermissao());
    }
}
