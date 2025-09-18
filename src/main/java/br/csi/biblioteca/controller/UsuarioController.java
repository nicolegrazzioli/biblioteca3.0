package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){ //retorna resposta http completa, com objeto Usuario
        Usuario u = service.salvar(usuario);
            //cod 201 (criado)         objeto u na resposta (resultado)
        return ResponseEntity.status(HttpStatus.CREATED).body(u);
    }


    //admin atualizae um usuario
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Integer id, @RequestBody Usuario usuario){
        usuario.setIdUs(id);
        Usuario u = service.atualizar(usuario);
        return ResponseEntity.ok(u);
    }


    //admin excluir usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id){
        service.excluir(id);
        return ResponseEntity.noContent().build(); //status 204 - exclusão bem sucedida
    }


    //admin listar usuarios ativos
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = service.listarAtivos();
        return ResponseEntity.ok(usuarios); //ResponseEntity.status(HttpStatus.OK) = 200
    }


    //admin buscar usuario por id
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id){
        Usuario u = service.getUsuarioById(id);
        return ResponseEntity.ok(u);
    }
}
