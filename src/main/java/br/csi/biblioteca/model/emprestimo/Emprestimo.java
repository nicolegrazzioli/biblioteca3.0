package br.csi.biblioteca.model.emprestimo;

import br.csi.biblioteca.model.livro.Livro;
import br.csi.biblioteca.model.usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emp")
    private int idEmp;

    // emprestimo - livro
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_livro_emp")
    private Livro livroEmp;

    // emprestimo - usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_emp")
    private Usuario usuarioEmp;

    @Column(name = "data_emprestimo_emp", nullable = false)
    private LocalDate dataEmprestimoEmp;

    @Column(name = "data_devolucao_prevista_emp", nullable = false)
    private LocalDate dataDevolucaoPrevistaEmp;

    @Column(name = "data_devolucao_efetiva_emp")
    private LocalDate dataDevolucaoEfetivaEmp;

    @Column(name = "status_emp", nullable = false)
    private String statusEmp;

}
