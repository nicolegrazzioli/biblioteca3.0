package br.csi.biblioteca.model.emprestimo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {
    /** findBy -- inicia uma consulta
     *  UsuarioEmp -- atributo UsuarioEmp
     *  _ -- proximo atributo
     *  IdUs -- proximo atributo */

    List<Emprestimo> findByUsuarioEmp_IdUs(Integer idUs);
}
