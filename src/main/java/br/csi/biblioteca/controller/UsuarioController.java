package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.DadosUsuario;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
//ok
/** status
 * POST = 201 CREATED
 * DELETE = 204 NO CONTENT
 * GET & PUT = 200 OK
 */

@RestController //retorna dados no http
@RequestMapping("/usuarios")
public class UsuarioController {
    private UsuarioService service;
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }


    //criar ususario
    @PostMapping("/registrar")
//    public void salvar(@RequestBody Usuario usuario) { this.service.salvar(usuario); }
                                            //pega o json e transforma em um objeto Usuario
    public ResponseEntity<DadosUsuario> salvar(@Valid @RequestBody Usuario usuario, UriComponentsBuilder uriBuilder){ //retorna resposta http completa, com objeto Usuario
//        Usuario u = service.salvar(usuario);
            //cod 201 (criado)         objeto u na resposta (resultado)
//        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(usuario));

        Usuario usuarioSalvo = service.salvar(usuario);
        DadosUsuario dadosUsuario = new DadosUsuario(usuarioSalvo);
        URI uri = uriBuilder.path("/usuario/{id}").buildAndExpand(usuario.getIdUs()).toUri();
        return ResponseEntity.created(uri).body(dadosUsuario);
        /*{
            "emailUs": "aut@aut",
            "senhaUs": "aut",
            "nomeUs": "aut",
            "ativoUs": "true",
            "tipoUs": "USUARIO",
            "permissao": "role_usuario"
        }*/
    }


    //admin atualizae um usuario
    @PutMapping("/{id}")
    public ResponseEntity<DadosUsuario> atualizar(@PathVariable Integer id, @Valid @RequestBody Usuario usuario){
        usuario.setIdUs(id);
        Usuario usuarioAtualizado = service.atualizar(usuario);
        return ResponseEntity.ok(new DadosUsuario(usuarioAtualizado));
    }


    //admin excluir usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){
        service.excluir(id);
        return ResponseEntity.noContent().build(); //status 204 - exclusão bem sucedida
    }


    //admin listar usuarios ativos
    @GetMapping
    public ResponseEntity<List<DadosUsuario>> listar() {
//        List<Usuario> usuarios = service.listarAtivos();
        return ResponseEntity.ok(service.listarAtivos()); //ResponseEntity.status(HttpStatus.OK) = 200
    }


    //admin buscar usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<DadosUsuario> buscarPorId(@PathVariable Integer id){
        Usuario u = service.getUsuarioById(id);
        return ResponseEntity.ok(new DadosUsuario(u));
    }
}
