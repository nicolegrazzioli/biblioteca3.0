package br.csi.biblioteca.controller;

import br.csi.biblioteca.model.emprestimo.Emprestimo;
import br.csi.biblioteca.model.usuario.Usuario;
import br.csi.biblioteca.service.EmprestimoService;
import br.csi.biblioteca.service.LivroService;
import br.csi.biblioteca.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** status
 * POST = 201 CREATED
 * DELETE = 204 NO CONTENT
 * GET & PUT = 200 OK
 */

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {
    private EmprestimoService emprestimoService;
    private LivroService livroService;
    private UsuarioService usuarioService;
    public EmprestimoController(EmprestimoService emprestimoService, LivroService livroService, UsuarioService usuarioService) {
        this.emprestimoService = emprestimoService;
        this.livroService = livroService;
        this.usuarioService = usuarioService;
    }

    /* simula o tipo de usuario pela url, ja que nao tem login explicito
    * ex: http://localhost:8080/biblioteca3.0/emprestimos?idUsuario=1 (admin)
    * ex: http://localhost:8080/biblioteca3.0/emprestimos?idUsuario=2 (usuario)
     */
    @GetMapping
    public ResponseEntity<List<Emprestimo>> listar(@RequestParam Integer idUsuario) {
        List<Emprestimo> emprestimos =  emprestimoService.listar(idUsuario);
        return ResponseEntity.ok(emprestimos); //200
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Integer id) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimo); //200
    }

    /*
    RequestBody - pega o conteudo (json) e coloca numa variavel
     */
    @PostMapping("/registrar")
    public ResponseEntity<Emprestimo> salvar(@RequestBody EmprestimoDTO emprestimoDTO) {
        Emprestimo novo = emprestimoService.criarEmprestimo(emprestimoDTO.getLivroEmp(), emprestimoDTO.getUsuarioEmp());
        return ResponseEntity.status(HttpStatus.CREATED).body(novo); //201
    }






}
