package br.csi.biblioteca.model.usuario;

public record DadosUsuarioCompleto(int idUs, String nomeUs, String emailUs, boolean ativoUs, String permissao) {
    public DadosUsuarioCompleto(Usuario usuario) {
        this(usuario.getIdUs(), usuario.getNomeUs(), usuario.getEmailUs(), usuario.isAtivoUs(), usuario.getPermissao());
    }
}
